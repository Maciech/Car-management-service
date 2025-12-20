package com.car.management.cars;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CarController {

    CarRepository carRepository;

    @PostMapping
    public ResponseEntity<?> createCarRecord(@RequestBody CarDto carDto) {
        CarEntity carEntity = new CarEntity();
        carEntity.brand = carDto.brand;
        carEntity.model = carDto.model;
        carEntity.productionYear = carDto.productionYear;
        carEntity.isSold = carDto.isSold;
        carEntity.purchasePrice = carDto.purchasePrice;
        carEntity.salePrice = carDto.salePrice;

        carRepository.save(carEntity);
        return ResponseEntity.ok().body(carDto);
    }

    @GetMapping
    public ResponseEntity<?> getAllCars() {
        return ResponseEntity.ok().body(carRepository.findAll());
    }
}
