package com.car.management.cars.car;

import com.car.management.cars.car_search.criteria_enums.CarColor;
import com.car.management.utils.DefaultDatabaseFields;
import com.car.management.utils.GenerateMetaModel;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@GenerateMetaModel
public class CarEntity extends DefaultDatabaseFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long carId;

    String brand;
    String model;
    String generation;

    @Enumerated(EnumType.STRING)
    CarColor color;
    Integer productionYear;
    String vinNumber;
    Integer mileage;
    Integer kWPower;
    Integer engineCapacity;

    boolean isImported;
    boolean isDamaged;
    Integer numberOfPreviousOwners;
    String description;
    String owner;
    Long userId;

    BigDecimal purchasePrice;
    BigDecimal salePrice;
    boolean isSold;

    @Enumerated(EnumType.STRING)
    CarStatus status;

}
