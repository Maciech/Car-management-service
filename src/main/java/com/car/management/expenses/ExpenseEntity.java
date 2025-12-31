package com.car.management.expenses;

import com.car.management.cars.car.CarEntity;
import com.car.management.utils.DefaultDatabaseFields;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class ExpenseEntity extends DefaultDatabaseFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long expenseId;

    String type;
    Date date;
    String description;
    int amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    CarEntity car;
}
