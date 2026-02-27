package com.car.management.cars.car_search;

import com.car.management.cars.car.CarDto;
import com.car.management.cars.car.CarEntity;
import com.car.management.cars.car.CarRepository;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
