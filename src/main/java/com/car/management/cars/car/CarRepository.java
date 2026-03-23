package com.car.management.cars.car;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, Long>, JpaSpecificationExecutor<CarEntity> {

    List<CarEntity> findAllByOwnerOrderByCreationDate(String owner);
}
