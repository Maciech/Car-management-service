package com.car.management.cars.car_attachment;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<@NonNull AttachmentEntity, @NonNull Long> {

    List<AttachmentEntity> findAllByCarId(Long carId);
}
