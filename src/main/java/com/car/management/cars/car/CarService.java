package com.car.management.cars.car;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CarService {

    CarRepository carRepository;

    public CarDto createCarRecord(CarDto carDto) {
        CarEntity carEntity = new CarEntity();
        carEntity.setBrand(carDto.getBrand());
        carEntity.setModel(carDto.getModel());
        carEntity.setProductionYear(carDto.getProductionYear());
        carEntity.setSold(carDto.getIsSold());
        carEntity.setPurchasePrice(carDto.getPurchasePrice());
        carEntity.setSalePrice(carDto.getSalePrice());

        carRepository.save(carEntity);
        carDto.setCarId(carEntity.getCarId());
        return carDto;
    }

    public List<CarEntity> getAllCars() {
        return carRepository.findAll();
    }

    public CarEntity getCarById(Long carId) {
        return carRepository.findById(carId).orElseThrow();
    }


}
