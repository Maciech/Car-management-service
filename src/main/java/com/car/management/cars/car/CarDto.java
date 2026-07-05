package com.car.management.cars.car;

import com.car.management.cars.car_search.criteria_enums.CarColor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CarDto {

    Long carId;
    String brand;
    String model;
    String generation;
    Integer productionYear;
    Integer mileage;
    Integer kWPower;
    Integer engineCapacity;
    CarColor color;
    String vinNumber;
    Integer numberOfPreviousOwners;
    String description;
    BigDecimal purchasePrice;
    BigDecimal salePrice;
    Boolean isSold;
    Boolean isImported;
    Boolean isDamaged;
    CarStatus status;
    String[] images;
    BigDecimal totalExpenses;
    @JsonProperty("isOwner")
    Boolean isOwner;

}
