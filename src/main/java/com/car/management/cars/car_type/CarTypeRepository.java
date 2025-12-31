package com.car.management.cars.car_type;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarTypeRepository extends JpaRepository<CarTypeEntity, Long> {

    List<CarTypeEntity> findAllByBrand(String brand);

    boolean existsByBrandAndModelAndCarBody(String brand, String model, CarBody carBody);
}
