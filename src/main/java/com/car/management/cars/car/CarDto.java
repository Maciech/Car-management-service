package com.car.management.cars.car;

import com.car.management.cars.car_search.criteria_enums.CarColor;
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
    Integer kwPower;
    Integer engineCapacity;
    CarColor color;
    BigDecimal purchasePrice;
    BigDecimal salePrice;
    Boolean isSold;
    Boolean isImported;
    Boolean isDamaged;
    CarStatus status;
    String[] images;

}
