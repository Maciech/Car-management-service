package com.car.management.cars.car;

import com.car.management.expenses.ExpensesRepository;
import com.car.management.invitation.CarAccessEntity;
import com.car.management.invitation.CarAccessRepository;
import com.car.management.users.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CarService {

    CarRepository carRepository;
    ExpensesRepository expensesRepository;
    CarAccessRepository carAccessRepository;
    UserRepository userRepository;
    ModelMapper modelMapper;
    RestClient restClient;
    ObjectMapper objectMapper;

    // ── NHTSA records ──────────────────────────────────────────────────────────

    public record ManufacturerResponse(
            @JsonProperty("Results") List<ManufacturerResult> results
    ) {}

    public record ManufacturerResult(
            @JsonProperty("Mfr_CommonName") String commonName
    ) {}

    public record ModelResponse(
            @JsonProperty("Results") List<ModelResult> results
    ) {}

    public record ModelResult(
            @JsonProperty("Model_Name") String modelName
    ) {}

    // ── CarQuery records ───────────────────────────────────────────────────────

    private record CarQueryModelsResponse(
            @JsonProperty("Models") List<CarQueryModelEntry> models
    ) {}

    private record CarQueryModelEntry(
            @JsonProperty("model_year") String modelYear,
            @JsonProperty("model_name") String modelName
    ) {}

    private record CarQueryTrimsResponse(
            @JsonProperty("Trims") List<CarQueryTrimEntry> trims
    ) {}

    private record CarQueryTrimEntry(
            @JsonProperty("model_trim") String modelTrim
    ) {}

    // ── Car CRUD ───────────────────────────────────────────────────────────────

    public CarDto createCarRecord(CarDto carDto) {
        CarEntity newCar = modelMapper.map(carDto, CarEntity.class);
        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        userRepository.findByEmail(email).ifPresent(u -> newCar.setUserId(u.getUserId()));
        carRepository.save(newCar);
        carDto.setCarId(newCar.getCarId());
        return carDto;
    }

    public List<CarEntity> getAllCars() {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        return userRepository.findByEmail(email).map(user -> {
            Long userId = user.getUserId();

            List<Long> sharedIds = carAccessRepository.findAllByUserId(userId)
                    .stream().map(CarAccessEntity::getCarId).toList();

            Map<Long, CarEntity> result = new LinkedHashMap<>();
            carRepository.findByUserId(userId).forEach(c -> result.put(c.getCarId(), c));
            carRepository.findAllById(sharedIds).forEach(c -> result.putIfAbsent(c.getCarId(), c));
            return new ArrayList<>(result.values());
        }).orElseGet(() -> {
            // użytkownik nie istnieje w DB — zwróć pustą listę (bezpieczne)
            return new ArrayList<>();
        });
    }

    public CarEntity getCarById(Long carId) {
        return carRepository.findById(carId).orElseThrow();
    }

    public CarEntity updateCar(Long id, CarDto dto) {
        CarEntity car = carRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Car not found: " + id));

        modelMapper.map(dto, car);

        return carRepository.save(car);
    }

    // ── External APIs ──────────────────────────────────────────────────────────

    public Set<String> getAllManufacturersNames() {
        String url = "https://vpic.nhtsa.dot.gov/api/vehicles/GetAllManufacturers?format=json";
        ManufacturerResponse response = restClient.get().uri(url).retrieve().body(ManufacturerResponse.class);
        if (response != null && response.results() != null) {
            return response.results().stream()
                    .map(ManufacturerResult::commonName)
                    .filter(name -> name != null && !name.isBlank())
                    .collect(Collectors.toSet());
        }
        return Set.of();
    }

    public Set<String> getAllModelByMake(String make) {
        String url = "https://vpic.nhtsa.dot.gov/api/vehicles/GetModelsForMake/" + make + "?format=json";
        ModelResponse response = restClient.get().uri(url).retrieve().body(ModelResponse.class);
        if (response != null && response.results() != null) {
            return response.results().stream()
                    .map(ModelResult::modelName)
                    .filter(name -> name != null && !name.isBlank())
                    .collect(Collectors.toSet());
        }
        return Set.of();
    }

    public YearRangeDto getYearRange(String make, String model) {
        int currentYear = Year.now().getValue();
        YearRangeDto fallback = new YearRangeDto(1990, currentYear + 1);

        try {
            String url = "https://www.carqueryapi.com/api/0.3/?cmd=getModels"
                    + "&make=" + make.toLowerCase().replace(" ", "+")
                    + "&model=" + model.replace(" ", "+");

            String raw = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(String.class);

            if (raw == null || raw.isBlank()) return fallback;

            // Strip JSONP wrapper: ?({...}); or callback({...});
            String json = raw.trim();
            int start = json.indexOf('(');
            int end = json.lastIndexOf(')');
            if (start >= 0 && end > start) {
                json = json.substring(start + 1, end);
            }

            CarQueryModelsResponse response = objectMapper.readValue(json, CarQueryModelsResponse.class);

            if (response.models() == null || response.models().isEmpty()) return fallback;

            int min = response.models().stream()
                    .map(CarQueryModelEntry::modelYear)
                    .filter(y -> y != null && y.matches("\\d{4}"))
                    .mapToInt(Integer::parseInt)
                    .min()
                    .orElse(1990);

            int max = response.models().stream()
                    .map(CarQueryModelEntry::modelYear)
                    .filter(y -> y != null && y.matches("\\d{4}"))
                    .mapToInt(Integer::parseInt)
                    .max()
                    .orElse(currentYear);

            return new YearRangeDto(min, Math.max(max, currentYear));

        } catch (Exception e) {
            return fallback;
        }
    }

    public Set<String> getTrims(String make, String modelLine, int year) {
        try {
            String carQueryModel = modelLine.toLowerCase().replace(" ", "-");
            String url = "https://www.carqueryapi.com/api/0.3/?cmd=getTrims"
                    + "&make=" + make.toLowerCase().replace(" ", "+")
                    + "&model=" + carQueryModel
                    + "&year=" + year;

            String raw = restClient.get().uri(url).retrieve().body(String.class);
            if (raw == null || raw.isBlank()) return Set.of();

            String json = raw.trim();
            int start = json.indexOf('(');
            int end = json.lastIndexOf(')');
            if (start >= 0 && end > start) json = json.substring(start + 1, end);

            CarQueryTrimsResponse response = objectMapper.readValue(json, CarQueryTrimsResponse.class);
            if (response.trims() == null) return Set.of();

            return response.trims().stream()
                    .map(CarQueryTrimEntry::modelTrim)
                    .filter(t -> t != null && !t.isBlank())
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            return Set.of();
        }
    }

    // ── Financial summary ──────────────────────────────────────────────────────

    public FinancialSummaryDto getFinancialSummary(Long carId) {
        CarEntity car = carRepository.findById(carId).orElseThrow();

        BigDecimal totalExpenses = expensesRepository.findAllByCarCarId(carId).stream()
                .map(e -> BigDecimal.valueOf(e.getAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal purchasePrice = car.getPurchasePrice() != null ? car.getPurchasePrice() : BigDecimal.ZERO;
        BigDecimal investedAmount = purchasePrice.add(totalExpenses);

        BigDecimal salePrice = car.getSalePrice();
        BigDecimal profit = salePrice != null ? salePrice.subtract(investedAmount) : null;
        BigDecimal roi = (profit != null && investedAmount.compareTo(BigDecimal.ZERO) > 0)
                ? profit.divide(investedAmount, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                : null;

        LocalDateTime from = car.getCreationDate() != null ? car.getCreationDate() : LocalDateTime.now();
        long daysHeld = ChronoUnit.DAYS.between(from, LocalDateTime.now());

        return new FinancialSummaryDto(carId, purchasePrice, totalExpenses, investedAmount, salePrice, profit, roi, daysHeld);
    }
}
