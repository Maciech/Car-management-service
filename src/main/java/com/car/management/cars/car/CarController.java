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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCar(@PathVariable Long id, @RequestBody CarDto carDto) {
        return ResponseEntity.ok().body(carService.updateCar(id, carDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAllMake")
    public ResponseEntity<?> getAllMake() {
        return ResponseEntity.ok().body(carService.getAllManufacturersNames());
    }

    @GetMapping("/getAllModelByMake/{make}")
    public ResponseEntity<?> getAllModelByMake(@PathVariable String make) {
        return ResponseEntity.ok().body(carService.getAllModelByMake(make));
    }

    @GetMapping("/{id}/financial-summary")
    public ResponseEntity<?> getFinancialSummary(@PathVariable Long id) {
        return ResponseEntity.ok().body(carService.getFinancialSummary(id));
    }

    @GetMapping("/getYearRange")
    public ResponseEntity<?> getYearRange(
            @RequestParam String make,
            @RequestParam String model) {
        return ResponseEntity.ok().body(carService.getYearRange(make, model));
    }

    @GetMapping("/getTrims")
    public ResponseEntity<?> getTrims(
            @RequestParam String make,
            @RequestParam String modelLine,
            @RequestParam int year) {
        return ResponseEntity.ok().body(carService.getTrims(make, modelLine, year));
    }
}
