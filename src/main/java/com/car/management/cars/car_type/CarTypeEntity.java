package com.car.management.cars.car_type;

import com.car.management.utils.DefaultDatabaseFields;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class CarTypeEntity extends DefaultDatabaseFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long carTypeId;

    String brand;
    String model;
    CarBody carBody;
    Boolean isAutomatic;
    BigDecimal capacity;
    Integer power;
    Drive drive;

}
