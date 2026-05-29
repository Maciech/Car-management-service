package com.car.management.statistics;

import java.math.BigDecimal;
import java.util.Map;

public record StatisticsDto(
        long totalCars,
        long activeCars,
        long soldCars,
        BigDecimal totalInvestedInActive,
        BigDecimal soldRevenue,
        BigDecimal totalProfit,
        Map<String, Long> carsByStatus
) {}
