package com.car.management.cars.car;

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

    CarService carService;

    @PostMapping
    public ResponseEntity<?> createCarRecord(@RequestBody CarDto carDto) {
        return ResponseEntity.ok().body(carService.createCarRecord(carDto));
    }

    @GetMapping
    public ResponseEntity<?> getAllCars() {
        return ResponseEntity.ok().body(carService.getAllCars());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCarDetailById(@PathVariable Long id) {
        return ResponseEntity.ok().body(carService.getCarById(id));
    }

    @GetMapping("/getAllMake")
    public ResponseEntity<?> getAllMake() {
        return ResponseEntity.ok().body(carService.getAllManufacturersNames());
    }

    @GetMapping("/getAllModelByMake/{make}")
    public ResponseEntity<?> getAllModelByMake(@PathVariable String make) {
        return ResponseEntity.ok().body(carService.getAllModelByMake(make));
    }
}
