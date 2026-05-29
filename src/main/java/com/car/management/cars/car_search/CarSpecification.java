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

            if (criteria.getGeneration() != null && !criteria.getGeneration().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("generation")),
                        "%" + criteria.getGeneration().toLowerCase() + "%"
                ));
            }

            if (criteria.getColor() != null) {
                predicates.add(cb.equal(root.get(CarEntity_.COLOR), criteria.getColor()));
            }

            if (criteria.getMinPrice() != null && criteria.getMaxPrice() != null) {
                predicates.add(cb.between(root.get(CarEntity_.SALE_PRICE), criteria.getMinPrice(), criteria.getMaxPrice()));
            } else if (criteria.getMinPrice() != null) {
                predicates.add(cb.ge(root.get(CarEntity_.SALE_PRICE), criteria.getMinPrice()));
            } else if (criteria.getMaxPrice() != null) {
                predicates.add(cb.le(root.get(CarEntity_.SALE_PRICE), criteria.getMaxPrice()));
            }

            if (criteria.getMinYear() != null && criteria.getMaxYear() != null) {
                predicates.add(cb.between(root.get(CarEntity_.PRODUCTION_YEAR), criteria.getMinYear(), criteria.getMaxYear()));
            } else if (criteria.getMinYear() != null) {
                predicates.add(cb.ge(root.get(CarEntity_.PRODUCTION_YEAR), criteria.getMinYear()));
            } else if (criteria.getMaxYear() != null) {
                predicates.add(cb.le(root.get(CarEntity_.PRODUCTION_YEAR), criteria.getMaxYear()));
            }

            if (criteria.getMinPower() != null && criteria.getMaxPower() != null) {
                predicates.add(cb.between(root.get(CarEntity_.K_WPOWER), criteria.getMinPower(), criteria.getMaxPower()));
            } else if (criteria.getMinPower() != null) {
                predicates.add(cb.ge(root.get(CarEntity_.K_WPOWER), criteria.getMinPower()));
            } else if (criteria.getMaxPower() != null) {
                predicates.add(cb.le(root.get(CarEntity_.K_WPOWER), criteria.getMaxPower()));
            }

            if (criteria.getMinMileage() != null && criteria.getMaxMileage() != null) {
                predicates.add(cb.between(root.get(CarEntity_.MILEAGE), criteria.getMinMileage(), criteria.getMaxMileage()));
            } else if (criteria.getMinMileage() != null) {
                predicates.add(cb.ge(root.get(CarEntity_.MILEAGE), criteria.getMinMileage()));
            } else if (criteria.getMaxMileage() != null) {
                predicates.add(cb.le(root.get(CarEntity_.MILEAGE), criteria.getMaxMileage()));
            }

            if (criteria.getIsImported() != null) {
                predicates.add(cb.equal(root.get(CarEntity_.IS_IMPORTED), criteria.getIsImported()));
            }

            if (criteria.getIsDamaged() != null) {
                predicates.add(cb.equal(root.get(CarEntity_.IS_DAMAGED), criteria.getIsDamaged()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
