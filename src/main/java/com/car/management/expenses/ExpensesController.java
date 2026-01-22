package com.car.management.expenses;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cars/expenses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExpensesController {

    ExpensesService expensesService;

    @GetMapping("/{expenseId}")
    public ResponseEntity<?> getAllExpensesById(@PathVariable Long expenseId) {
        return ResponseEntity.ok().body(expensesService.getAllExpensesByCarId(expenseId));
    }


    @PutMapping
    public ResponseEntity<?> updateCarExpensesById(@RequestBody ExpenseDto expenseDto) {
        return ResponseEntity.ok().body(expensesService.updateCarExpensesById(expenseDto));
    }

    @PostMapping
    public ResponseEntity<?> createCarExpensesByCarId(@RequestBody ExpenseDto expenseDto) {
        return ResponseEntity.ok().body(expensesService.createCarExpensesByCarId(expenseDto));
    }
}
