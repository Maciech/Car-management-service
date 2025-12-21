package com.car.management.expenses;

import com.car.management.cars.CarEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class ExpenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long expenseId;

    String type;
    Date date;
    String description;
    int amount;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    CarEntity carEntity;
}
