package com.car.management.cars.car_image;

import com.car.management.cars.car.CarRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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

    private static final Path UPLOADS_DIR = Paths.get("uploads");

    public void saveImagesByCarId(Long carId, List<MultipartFile> images) {

        if (!carRepository.existsById(carId)) {
            throw new EntityNotFoundException("Car not found");
        }

        Path carDir = UPLOADS_DIR.resolve("cars").resolve(carId.toString());

        try {
            Files.createDirectories(carDir);
        } catch (IOException e) {
            throw new RuntimeException("Cannot create directory: " + carDir, e);
        }

        for (MultipartFile file : images) {
            String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
            Path target = carDir.resolve(filename);

            // 🔥 TYLKO RELATYWNA ŚCIEŻKA
            String url = "cars/" + carId + "/" + filename;

            try {
                file.transferTo(target);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save file", e);
            }

            ImageEntity imageEntity = ImageEntity.builder()
                    .carId(carId)
                    .url(url)
                    .position(0)
                    .build();

            imageRepository.save(imageEntity);
        }
    }

    public List<ImageDto> getImagesByCarId(Long carId) {
        return imageRepository.findAllByCarId(carId).stream()
                .map(image -> {
                    ImageDto dto = modelMapper.map(image, ImageDto.class);
                    dto.setUrl("http://localhost:8080/uploads/" + image.getUrl());
                    return dto;
                })
                .toList();
    }

    @Transactional
    public void deleteImageByImageId(Long imageId) {

        ImageEntity image = imageRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("Image not found"));

        Path filePath = UPLOADS_DIR.resolve(image.getUrl()).normalize();

        if (!filePath.startsWith(UPLOADS_DIR)) {
            throw new SecurityException("Invalid file path");
        }

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image file: " + filePath, e);
        }

        imageRepository.delete(image);
    }
}

