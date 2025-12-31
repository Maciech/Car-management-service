package com.car.management.cars.car_type;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/car-type")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CarTypeController {

    CarTypeService carTypeService;

    @GetMapping
    public ResponseEntity<?> getAllBrands() {
        return ResponseEntity.ok().body(carTypeService.getAllBrands());
    }

    @GetMapping(value = "/{brand}")
    public ResponseEntity<?> getAllModelsByBrand(@PathVariable String brand) {
        return ResponseEntity.ok().body(carTypeService.getAllModelsByBrand(brand));
    }

    @PostMapping
    public ResponseEntity<?> createCarRecord(@RequestBody CarTypeDto carTypeDto) {
        return ResponseEntity.ok().body(carTypeService.createCarTypeRecord(carTypeDto));
    }
}
