package com.car.management.cars.car_search;

import com.car.management.cars.car.CarEntity;
import com.car.management.cars.car.CarEntity_;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class CarSpecification {

    public static Specification<@NonNull CarEntity> build(SearchCriteria criteria) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getBrand() != null) {
                predicates.add(cb.equal(root.get(CarEntity_.BRAND), criteria.getBrand()));
            }

            if (criteria.getModel() != null) {
                predicates.add(cb.equal(root.get(CarEntity_.MODEL), criteria.getModel()));
            }

            if (criteria.getColor() != null) {
                predicates.add(cb.equal(root.get(CarEntity_.COLOR), criteria.getColor()));
            }

            if (criteria.getMinPrice() != null || criteria.getMaxPrice() != null) {
                predicates.add(cb.equal(root.get(CarEntity_.COLOR), criteria.getColor()));
            }



            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
