package com.car.management.cars.car_type;

import com.car.management.utils.DefaultDatabaseFields;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CarTypeDto extends DefaultDatabaseFields {

    Long carTypeId;
    String brand;
    String model;
    CarBody carBody;
    Boolean isAutomatic;
    BigDecimal capacity;
    Integer power;
    Drive drive;

}
