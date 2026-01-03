package com.car.management.cars.car_image;

import com.car.management.cars.car.CarRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageService {

    ImageRepository imageRepository;
    CarRepository carRepository;
    ModelMapper modelMapper;


    public void saveImagesByCarId(Long carId, List<MultipartFile> images) throws IOException {
        if (!carRepository.existsById(carId)) {
            throw new EntityNotFoundException();
        }

        Path baseDir = Paths.get("uploads/cars/" + carId);
        Files.createDirectories(baseDir);


        images.forEach((multipartFile) -> {
            String filename = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
            Path target = baseDir.resolve(filename);
            String url = "/uploads/cars/" + carId + "/" + filename;

            ImageEntity imageEntity = ImageEntity.builder()
                    .url(url)
                    .carId(carId)
                    .position(0)
                    .build();

            try {
                multipartFile.transferTo(target);
                imageRepository.save(imageEntity);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public List<ImageDto> getImagesByCarId(Long carId) {
        List<ImageDto> imageDtos = imageRepository.findAllByCarId(carId).stream()
                .map(imageEntity -> modelMapper.map(imageEntity, ImageDto.class))
                .toList();
        imageDtos.forEach(imageDto -> {
            String url = imageDto.getUrl();
            imageDto.setUrl("http://localhost:8080" + url);
        });
        return imageDtos;

    }

    public void deleteImageByImageId(Long imageId) {
        ImageEntity imageEntity = imageRepository.findById(imageId).orElseThrow();
        Path filePath = Paths.get("uploads").resolve(imageEntity.getUrl());

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image file: " + filePath, e);
        }

        imageRepository.deleteById(imageId);
    }
}
