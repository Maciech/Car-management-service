package com.car.management.expenses;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpensesRepository extends JpaRepository<@NonNull ExpenseEntity, @NonNull Long> {

    List<ExpenseEntity> findAllByCarCarId(Long carId);
}
