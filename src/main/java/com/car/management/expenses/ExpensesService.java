package com.car.management.expenses;

import com.car.management.cars.car.CarRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExpensesService {

    ExpensesRepository expensesRepository;
    CarRepository carRepository;
    ModelMapper modelMapper;

    public List<ExpenseEntity> getAllExpensesByCarId(Long carId) {
        return expensesRepository.findAllByCarCarId(carId);
    }

    public ExpenseEntity updateCarExpensesById(ExpenseDto expenseDto) {
        ExpenseEntity expenseEntity = expensesRepository.findById(expenseDto.getExpenseId())
                .orElseThrow();

        modelMapper.map(expenseDto, expenseEntity);
        System.out.println(expenseEntity);

    return expenseEntity;
    }

    public ExpenseEntity createCarExpensesByCarId(ExpenseDto expenseDto) {
        if (!carRepository.existsById(expenseDto.getCarId())) {
            throw new EntityNotFoundException();
        }

        ExpenseEntity expenseEntity = modelMapper.map(expenseDto, ExpenseEntity.class);
        return expensesRepository.save(expenseEntity);
    }
}
