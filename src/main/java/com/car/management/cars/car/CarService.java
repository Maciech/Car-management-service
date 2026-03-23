package com.car.management.cars.car;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CarService {

    CarRepository carRepository;
    ModelMapper modelMapper;
    RestClient restClient;

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

    public CarDto createCarRecord(CarDto carDto) {
//        CarEntity carEntity = new CarEntity();
        CarEntity newCar = modelMapper.map(carDto, CarEntity.class);
//        carEntity.setBrand(carDto.getBrand());
//        carEntity.setModel(carDto.getModel());
//        carEntity.setProductionYear(carDto.getProductionYear());
//        carEntity.setMileage(carDto.getMileage());
//        carEntity.setKWPower(carDto.getKwPower());
//        carEntity.setSold(carDto.getIsSold());
//        carEntity.setPurchasePrice(carDto.getPurchasePrice());
//        carEntity.setSalePrice(carDto.getSalePrice());

        carRepository.save(newCar);
        carDto.setCarId(newCar.getCarId());
        return carDto;
    }

    public List<CarEntity> getAllCars() {
        return carRepository.findAll();
    }

    public Set<String> getAllManufacturersNames() {
        String url = "https://vpic.nhtsa.dot.gov/api/vehicles/GetAllManufacturers?format=json";

        // Wykonanie strzału i mapowanie na nasz rekord
        ManufacturerResponse response = restClient.get()
                .uri(url)
                .retrieve()
                .body(ManufacturerResponse.class);

        // Wyciąganie CommonName do Set<String>
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

        // Wykonanie strzału i mapowanie na nasz rekord
        ModelResponse response = restClient.get()
                .uri(url)
                .retrieve()
                .body(ModelResponse.class);

        // Wyciąganie CommonName do Set<String>
        if (response != null && response.results() != null) {
            return response.results().stream()
                    .map(ModelResult::modelName)
                    .filter(name -> name != null && !name.isBlank())
                    .collect(Collectors.toSet());
        }

        return Set.of();
    }

    public CarEntity getCarById(Long carId) {
        return carRepository.findById(carId).orElseThrow();
    }


}
