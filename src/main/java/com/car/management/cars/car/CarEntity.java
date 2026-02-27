package com.car.management.cars.car;

import com.car.management.cars.car_search.criteria_enums.CarColor;
import com.car.management.utils.DefaultDatabaseFields;
import com.car.management.utils.GenerateMetaModel;
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
@GenerateMetaModel
public class CarEntity extends DefaultDatabaseFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long carId;

    String brand;
    String model;
    CarColor color;
    int productionYear;
    String vinNumber;
    int mileage;
    int kWPower;
    int engineCapacity;

    boolean isImported;
    boolean isDamaged;
    int numberOfPreviousOwners;
    String description;
    String owner;

    BigDecimal purchasePrice;
    BigDecimal salePrice;
    boolean isSold;

}
