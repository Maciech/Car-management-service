package com.car.management.cars.car_search;

import com.car.management.cars.car.CarDto;
import com.car.management.cars.car.CarEntity;
import com.car.management.cars.car.CarRepository;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CarSearchService {

    CarRepository carRepository;
    ModelMapper modelMapper;

    public List<CarDto> search(SearchCriteria criteria) {

         Specification<@NonNull CarEntity> spec = CarSpecification.build(criteria);

        List<CarEntity> cars = carRepository.findAll(spec);

        return cars.stream()
                .map(carEntity -> modelMapper.map(carEntity, CarDto.class))
                .toList();
    }

    public @Nullable Map<String, Integer> extractBrandWithAmount() {
        List<BrandCount> brandCounts = carRepository.countCarsByBrand();
        Map<String, Integer> brandWithNumber = new HashMap<>();
        brandCounts.forEach(brandCount -> {
            brandWithNumber.put(brandCount.getName(), brandCount.getTotal());
        });
        return brandWithNumber;
    }

    public @Nullable Map<String, Integer> extractModelWithAmount(String brand) {
        List<ModelCount> modelCounts = carRepository.countCarsByModel(brand);
        Map<String, Integer> modelWithNumber = new HashMap<>();
        modelCounts.forEach(modelCount ->
                modelWithNumber.put(modelCount.getName(), modelCount.getTotal())
                );
        return  modelWithNumber;
    }

    public @Nullable Integer extractFoundByNameNumber(String brand, String model, Integer minYear, Integer maxYear) {
        return carRepository.extractFoundByNameNumber(brand, model, minYear, maxYear);
    }
}
