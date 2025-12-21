package com.car.management.costs;

import com.car.management.cars.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CostsRepository extends JpaRepository<CostsEntity, Long> {
}
