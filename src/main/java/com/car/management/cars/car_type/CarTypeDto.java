package com.car.management.cars.car_type;

import com.car.management.utils.DefaultDatabaseFields;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
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
