package com.car.management.cars.car;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CarService {

    CarRepository carRepository;
    ModelMapper modelMapper;

    public CarDto createCarRecord(CarDto carDto) {
//        CarEntity carEntity = new CarEntity();
        CarEntity newCar = modelMapper.map(carDto, CarEntity.class);
//        carEntity.setBrand(carDto.getBrand());
//        carEntity.setModel(carDto.getModel());
//        carEntity.setProductionYear(carDto.getProductionYear());
//        carEntity.setMileage(carDto.getMileage());
//        carEntity.setKWPower(carDto.getKwPower());
//        carEntity.setSold(carDto.getIsSold());
//        carEntity.setPurchasePrice(carDto.getPurchasePrice());
//        carEntity.setSalePrice(carDto.getSalePrice());

        carRepository.save(newCar);
        carDto.setCarId(newCar.getCarId());
        return carDto;
    }

    public List<CarEntity> getAllCars() {
        return carRepository.findAll();
    }

    public CarEntity getCarById(Long carId) {
        return carRepository.findById(carId).orElseThrow();
    }


}
