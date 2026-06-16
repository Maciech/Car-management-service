package com.car.management.cars.car_search;

import com.car.management.cars.car.CarDto;
import com.car.management.cars.car.CarEntity;
import com.car.management.cars.car.CarRepository;
import com.car.management.cars.car.CarStatus;
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

    /**
     * Publiczna wyszukiwarka — zwraca wyłącznie auta ze statusem WYSTAWIONE
     * (od wszystkich użytkowników). Dostępna bez logowania.
     */
    public List<CarDto> search(SearchCriteria criteria) {
        // Filtr użytkownika + obowiązkowy filtr: tylko auta wystawione na sprzedaż
        Specification<@NonNull CarEntity> spec = CarSpecification.build(criteria)
                .and((root, query, cb) ->
                        cb.equal(root.get("status"), CarStatus.WYSTAWIONE));

        return carRepository.findAll(spec).stream()
                .map(car -> modelMapper.map(car, CarDto.class))
                .toList();
    }
}
