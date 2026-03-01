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
    int productionYear;
    int mileage;
    int kwPower;
    CarColor color;
    BigDecimal purchasePrice;
    BigDecimal salePrice;
    Boolean isSold;
    Boolean isImported;
    Boolean isDamaged;

}
