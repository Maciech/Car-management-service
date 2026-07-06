package com.car.management.cars.car_image;

import com.car.management.cars.car.CarRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageService {

    ImageRepository imageRepository;
    CarRepository carRepository;
    ModelMapper modelMapper;

    @NonFinal
    @Value("${app.backend.url:http://localhost:8080}")
    String backendUrl;

    private static final Path UPLOADS_DIR = Paths.get("uploads");

    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );

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
            validateImageFile(file);

            String safeName = Paths.get(file.getOriginalFilename() != null ? file.getOriginalFilename() : "file").getFileName().toString();
            String filename = UUID.randomUUID() + "-" + safeName;
            Path target = carDir.resolve(filename).normalize();

            if (!target.startsWith(UPLOADS_DIR)) {
                throw new SecurityException("Invalid file path");
            }

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
                    dto.setUrl(backendUrl + "/uploads/" + image.getUrl());
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

    private void validateImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("File type not allowed: " + contentType);
        }
        try (InputStream is = file.getInputStream()) {
            byte[] header = new byte[8];
            if (is.read(header) < 4) throw new IllegalArgumentException("File too small");
            if (!hasAllowedMagicBytes(header)) throw new IllegalArgumentException("File content does not match allowed image types");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }

    private boolean hasAllowedMagicBytes(byte[] h) {
        // JPEG: FF D8 FF
        if (h[0] == (byte) 0xFF && h[1] == (byte) 0xD8 && h[2] == (byte) 0xFF) return true;
        // PNG: 89 50 4E 47
        if (h[0] == (byte) 0x89 && h[1] == 0x50 && h[2] == 0x4E && h[3] == 0x47) return true;
        // GIF: 47 49 46 38
        if (h[0] == 0x47 && h[1] == 0x49 && h[2] == 0x46 && h[3] == 0x38) return true;
        // WebP: RIFF....WEBP
        if (h[0] == 0x52 && h[1] == 0x49 && h[2] == 0x46 && h[3] == 0x46) return true;
        return false;
    }
}

