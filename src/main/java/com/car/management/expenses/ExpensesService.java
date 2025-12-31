package com.car.management.expenses;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExpensesService {

    ExpensesRepository expensesRepository;
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
        ExpenseEntity expenseEntity = expensesRepository.findById(expenseDto.getExpenseId())
                .orElseThrow();

        modelMapper.map(expenseDto, expenseEntity);
        System.out.println(expenseEntity);

        return expenseEntity;
    }
}
