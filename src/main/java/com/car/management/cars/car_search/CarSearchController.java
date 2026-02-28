package com.car.management.cars.car_search;

import com.car.management.cars.car.CarDto;
import com.car.management.cars.car_search.criteria_enums.CarColor;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
            @RequestParam(required = false) Integer maxMileage
    ) {

        SearchCriteria criteria = new SearchCriteria(
                brand, model, color, minPrice, maxPrice,
                minYear, maxYear, minPower, maxPower, minMileage, maxMileage
        );

        return ResponseEntity.ok(carSearchService.search(criteria));
    }

    @GetMapping("/extractBrandWithAmount")
    public ResponseEntity<Map<String, Integer>> extractBrandWithAmount() {
        return ResponseEntity.ok().body(carSearchService.extractBrandWithAmount());
    }

    @GetMapping("/extractModelWithAmount/{brand}")
    public ResponseEntity<Map<String, Integer>> extractModelWithAmount(@PathVariable String brand) {
        return ResponseEntity.ok().body(carSearchService.extractModelWithAmount(brand));
    }

    @GetMapping("/extractModelWithAmount/{brand}/{model}/{minYear}/{maxYear}")
    public ResponseEntity<Integer> extractFoundByNameNumber(@PathVariable String brand, @PathVariable String model,
                                                            @PathVariable Integer minYear,
                                                            @PathVariable Integer maxYear) {
        return ResponseEntity.ok().body(carSearchService.extractFoundByNameNumber(brand, model, minYear, maxYear));
    }
}
