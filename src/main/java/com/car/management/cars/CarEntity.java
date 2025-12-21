package com.car.management.cars;

import com.car.management.expenses.ExpenseEntity;
import com.car.management.utils.DefaultDatabaseFields;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class CarEntity extends DefaultDatabaseFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String brand;
    String model;
    int productionYear;
    String vinNumber;
    int mileage;
    int kWPower;
    int engineCapacity;

    boolean isImported;
    boolean isDamaged;
    int numberOfPreviousOwners;
    String description;


    BigDecimal purchasePrice;
    BigDecimal salePrice;
    boolean isSold;

    @OneToMany(
            mappedBy = "car",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<ExpenseEntity> carExpenses;

}
