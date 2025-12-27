package com.car.management.cars.car;

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
    int productionYear;
    BigDecimal purchasePrice;
    BigDecimal salePrice;
    Boolean isSold;

}
