package com.car.management.cars.car_image;

import com.car.management.expenses.ExpenseEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<@NonNull ImageEntity, @NonNull Long> {

    List<ImageEntity> findAllByCarId(Long carId);
}
