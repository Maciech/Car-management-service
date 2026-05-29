package com.car.management.cars.car_search;

import com.car.management.cars.car_search.criteria_enums.CarColor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteria {

    private String brand;
    private String model;
    private String generation;
    private CarColor color;
    private Integer minPrice;
    private Integer maxPrice;
    private Integer minYear;
    private Integer maxYear;
    private Integer minPower;
    private Integer maxPower;
    private Integer minMileage;
    private Integer maxMileage;
    private Boolean isImported;
    private Boolean isDamaged;
}