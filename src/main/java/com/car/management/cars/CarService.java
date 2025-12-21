package com.car.management.cars;

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
        carEntity.brand = carDto.getBrand();
        carEntity.model = carDto.getModel();
        carEntity.productionYear = carDto.getProductionYear();
        carEntity.isSold = carDto.getIsSold();
        carEntity.purchasePrice = carDto.getPurchasePrice();
        carEntity.salePrice = carDto.getSalePrice();

        carRepository.save(carEntity);
        carDto.setCarId(carEntity.getId());
        return carDto;
    }

    public List<CarEntity> getAllCars() {
        return carRepository.findAll();
    }

    public CarEntity getCarById(Long carId) {
        return carRepository.findById(carId).orElseThrow();
    }


}
