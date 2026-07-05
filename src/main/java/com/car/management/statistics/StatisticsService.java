package com.car.management.statistics;

import com.car.management.cars.car.CarEntity;
import com.car.management.cars.car.CarRepository;
import com.car.management.cars.car.CarStatus;
import com.car.management.expenses.ExpenseEntity;
import com.car.management.expenses.ExpensesRepository;
import com.car.management.invitation.CarAccessEntity;
import com.car.management.invitation.CarAccessRepository;
import com.car.management.users.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticsService {

    CarRepository carRepository;
    ExpensesRepository expensesRepository;
    UserRepository userRepository;
    CarAccessRepository carAccessRepository;

    // ── Pobierz auta dostępne dla zalogowanego użytkownika ─────────────────────

    private List<CarEntity> getUserCars() {
        String email = Objects.requireNonNull(
                SecurityContextHolder.getContext().getAuthentication()).getName();

        return userRepository.findByEmail(email).map(user -> {
            Long userId = user.getUserId();

            List<Long> sharedIds = carAccessRepository.findAllByUserId(userId)
                    .stream().map(CarAccessEntity::getCarId).toList();

            Map<Long, CarEntity> result = new LinkedHashMap<>();
            carRepository.findByUserId(userId).forEach(c -> result.put(c.getCarId(), c));
            carRepository.findAllById(sharedIds).forEach(c -> result.putIfAbsent(c.getCarId(), c));

            log.debug("Statystyki dla użytkownika {} — {} aut (własnych + współdzielonych)",
                    email, result.size());
            return new ArrayList<>(result.values());
        }).orElseGet(() -> {
            log.warn("Nie znaleziono użytkownika '{}' w bazie — statystyki puste", email);
            return new ArrayList<>();
        });
    }

    // ── Główna metoda ───────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public StatisticsDto getPortfolioStatistics() {
        List<CarEntity> userCars = getUserCars();

        if (userCars.isEmpty()) {
            return new StatisticsDto(0, 0, 0,
                    BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, Map.of());
        }

        List<Long> carIds = userCars.stream().map(CarEntity::getCarId).toList();

        // Pobierz wydatki tylko dla aut użytkownika
        List<ExpenseEntity> expenses = expensesRepository.findAllByCarCarIdIn(carIds);

        Map<Long, BigDecimal> expensesByCarId = expenses.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getCar().getCarId(),
                        Collectors.reducing(BigDecimal.ZERO,
                                e -> BigDecimal.valueOf(e.getAmount()),
                                BigDecimal::add)
                ));

        List<CarEntity> activeCars = userCars.stream().filter(c -> !c.isSold()).toList();
        List<CarEntity> soldCars   = userCars.stream().filter(CarEntity::isSold).toList();

        BigDecimal activePurchaseCost = activeCars.stream()
                .map(c -> c.getPurchasePrice() != null ? c.getPurchasePrice() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal activeExpensesSum = activeCars.stream()
                .map(c -> expensesByCarId.getOrDefault(c.getCarId(), BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalInvestedInActive = activePurchaseCost.add(activeExpensesSum);

        BigDecimal soldRevenue = soldCars.stream()
                .map(c -> c.getSalePrice() != null ? c.getSalePrice() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal soldPurchaseCost = soldCars.stream()
                .map(c -> c.getPurchasePrice() != null ? c.getPurchasePrice() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal soldExpensesSum = soldCars.stream()
                .map(c -> expensesByCarId.getOrDefault(c.getCarId(), BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalProfit = soldRevenue.subtract(soldPurchaseCost).subtract(soldExpensesSum);

        Map<String, Long> carsByStatus = userCars.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getStatus() != null ? c.getStatus().name() : CarStatus.GOTOWE.name(),
                        Collectors.counting()
                ));

        return new StatisticsDto(
                userCars.size(),
                activeCars.size(),
                soldCars.size(),
                totalInvestedInActive,
                soldRevenue,
                totalProfit,
                carsByStatus
        );
    }
}
