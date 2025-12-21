package com.car.management.expenses;

import com.car.management.cars.CarRepository;
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

//    @PostMapping
//    public ResponseEntity<?> createCarRecord(@RequestBody CarDto carDto) {
//        CarEntity carEntity = new CarEntity();
//        carEntity.brand = carDto.brand;
//        carEntity.model = carDto.model;
//        carEntity.productionYear = carDto.productionYear;
//        carEntity.isSold = carDto.isSold;
//        carEntity.purchasePrice = carDto.purchasePrice;
//        carEntity.salePrice = carDto.salePrice;
//
//        carRepository.save(carEntity);
//        return ResponseEntity.ok().body(carDto);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAllExpensesById(@PathVariable Long id) {
        return ResponseEntity.ok().body(carRepository.findAll());
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> updateCarExpenses(@PathVariable Long id) {
        return ResponseEntity.ok().body(carRepository.findAll());
    }
}
