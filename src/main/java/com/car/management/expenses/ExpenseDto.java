package com.car.management.expenses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ExpenseDto {

    Long expenseId;
    Long carId;
    String type;
    Date date;
    String description;
    String payer;
    int amount;
}
