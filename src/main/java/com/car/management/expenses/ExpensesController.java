package com.car.management.expenses;

import com.car.management.cars.car.CarRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cars/costs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExpensesController {

    CarRepository carRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getAllExpensesById(@PathVariable Long id) {
        return ResponseEntity.ok().body(carRepository.findAll());
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> updateCarExpenses(@PathVariable Long id) {
        return ResponseEntity.ok().body(carRepository.findAll());
    }
}
