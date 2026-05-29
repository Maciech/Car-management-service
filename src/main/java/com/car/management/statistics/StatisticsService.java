package com.car.management.statistics;

import com.car.management.cars.car.CarEntity;
import com.car.management.cars.car.CarRepository;
import com.car.management.cars.car.CarStatus;
import com.car.management.expenses.ExpenseEntity;
import com.car.management.expenses.ExpensesRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticsService {

    CarRepository carRepository;
    ExpensesRepository expensesRepository;

    @Transactional(readOnly = true)
    public StatisticsDto getPortfolioStatistics() {
        List<CarEntity> allCars = carRepository.findAll();
        List<ExpenseEntity> allExpenses = expensesRepository.findAll();

        Map<Long, BigDecimal> expensesByCarId = allExpenses.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getCar().getCarId(),
                        Collectors.reducing(BigDecimal.ZERO,
                                e -> BigDecimal.valueOf(e.getAmount()),
                                BigDecimal::add)
                ));

        List<CarEntity> activeCars = allCars.stream().filter(c -> !c.isSold()).toList();
        List<CarEntity> soldCars = allCars.stream().filter(CarEntity::isSold).toList();

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

        Map<String, Long> carsByStatus = allCars.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getStatus() != null ? c.getStatus().name() : CarStatus.GOTOWE.name(),
                        Collectors.counting()
                ));

        return new StatisticsDto(
                allCars.size(),
                activeCars.size(),
                soldCars.size(),
                totalInvestedInActive,
                soldRevenue,
                totalProfit,
                carsByStatus
        );
    }
}
