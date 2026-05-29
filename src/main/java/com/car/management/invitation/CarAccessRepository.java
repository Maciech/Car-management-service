package com.car.management.invitation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarAccessRepository extends JpaRepository<CarAccessEntity, Long> {

    List<CarAccessEntity> findAllByUserId(Long userId);
}
