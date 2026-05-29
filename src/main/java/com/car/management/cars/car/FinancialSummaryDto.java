package com.car.management.cars.car;

import java.math.BigDecimal;

public record FinancialSummaryDto(
        Long carId,
        BigDecimal purchasePrice,
        BigDecimal totalExpenses,
        BigDecimal investedAmount,
        BigDecimal salePrice,
        BigDecimal profit,
        BigDecimal roi,
        long daysHeld
) {}
