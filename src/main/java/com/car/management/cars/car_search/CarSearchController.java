package com.car.management.cars.car_search;

import com.car.management.cars.car.CarDto;
import com.car.management.cars.car_search.criteria_enums.CarColor;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.car.management.utils.Constants.CAR_SEARCH_API;

@RestController
@RequestMapping(CAR_SEARCH_API)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CarSearchController {

    CarSearchService carSearchService;

    @GetMapping("/search")
    public ResponseEntity<List<CarDto>> search(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) CarColor color,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer minYear,
            @RequestParam(required = false) Integer maxYear,
            @RequestParam(required = false) Integer minPower,
            @RequestParam(required = false) Integer maxPower,
            @RequestParam(required = false) Integer minMileage,
            @RequestParam(required = false) Integer maxMileage,
            @RequestParam(required = false) Boolean isImported,
            @RequestParam(required = false) Boolean isDamaged
    ) {

        SearchCriteria criteria = new SearchCriteria(
                brand, model, color, minPrice, maxPrice,
                minYear, maxYear, minPower, maxPower, minMileage, maxMileage, isImported, isDamaged
        );

        return ResponseEntity.ok(carSearchService.search(criteria));
    }
}
