package com.car.management.cars;

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

    String brand;
    String model;
    int productionYear;
    BigDecimal purchasePrice;
    BigDecimal salePrice;
    boolean isSold;

}
