package com.car.management.cars.car_type;

import com.car.management.cars.car.CarDto;
import com.car.management.cars.car.CarEntity;
import com.car.management.utils.CarManagementUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CarTypeService {

    CarTypeRepository carTypeRepository;
    ModelMapper modelMapper;

    public Map<String, Integer> getAllBrands() {
        List<CarTypeEntity> allCarTypes = carTypeRepository.findAll();

        Map<String, Integer> brandWithNumber = new HashMap();

        Map<String, List<CarTypeEntity>> brandWithCarTypeList = allCarTypes.stream()
                .collect(Collectors.groupingBy(CarTypeEntity::getBrand));
        brandWithCarTypeList.forEach((s, carTypeEntities) -> {
            brandWithNumber.put(s, carTypeEntities.size());
        });
        return brandWithNumber;
    }

    public List<String> getAllModelsByBrand(String brand) {
        return carTypeRepository.findAllByBrand(brand).stream()
                .map(CarTypeEntity::getModel)
                .toList();
    }

    public CarTypeDto createCarTypeRecord(CarTypeDto carTypeDto) {
        boolean recordAlreadyExist = carTypeRepository
                .existsByBrandAndModelAndCarBody(carTypeDto.getBrand(), carTypeDto.getModel(), carTypeDto.getCarBody());
        if (recordAlreadyExist) {
            return null;
        }
        setDefaultDatabaseFields(carTypeDto);
        CarTypeEntity carTypeEntity = modelMapper.map(carTypeDto, CarTypeEntity.class);

        carTypeRepository.save(carTypeEntity);
        carTypeDto.setCarTypeId(carTypeEntity.getCarTypeId());
        return carTypeDto;
    }

    private void setDefaultDatabaseFields(CarTypeDto carTypeDto) {
        carTypeDto.setActive(true);
        carTypeDto.setCreationDate(CarManagementUtils.getCurrentTime());
        carTypeDto.setUpdateDate(CarManagementUtils.getCurrentTime());
    }
}
