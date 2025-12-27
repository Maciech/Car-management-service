package com.car.management.cars.car_type;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
