package com.car.management.cars.car_attachment;

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
public class AttachmentService {

    AttachmentRepository attachmentRepository;
    CarRepository carRepository;
    ModelMapper modelMapper;

    private static final Path UPLOADS_DIR = Paths.get("uploads");

    public void saveAttachmentsByCarId(Long carId, List<MultipartFile> files) {
        if (!carRepository.existsById(carId)) {
            throw new EntityNotFoundException("Car not found");
        }

        Path carDir = UPLOADS_DIR.resolve("attachments").resolve(carId.toString());

        try {
            Files.createDirectories(carDir);
        } catch (IOException e) {
            throw new RuntimeException("Cannot create directory: " + carDir, e);
        }

        for (MultipartFile file : files) {
            String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
            Path target = carDir.resolve(filename);
            String url = "attachments/" + carId + "/" + filename;

            try {
                file.transferTo(target);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save file", e);
            }

            AttachmentEntity entity = AttachmentEntity.builder()
                    .carId(carId)
                    .url(url)
                    .originalName(file.getOriginalFilename())
                    .mimeType(file.getContentType())
                    .build();

            attachmentRepository.save(entity);
        }
    }

    public List<AttachmentDto> getAttachmentsByCarId(Long carId) {
        return attachmentRepository.findAllByCarId(carId).stream()
                .map(a -> {
                    AttachmentDto dto = modelMapper.map(a, AttachmentDto.class);
                    dto.setUrl("http://localhost:8080/uploads/" + a.getUrl());
                    return dto;
                })
                .toList();
    }

    @Transactional
    public void deleteAttachment(Long attachmentId) {
        AttachmentEntity entity = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new EntityNotFoundException("Attachment not found"));

        Path filePath = UPLOADS_DIR.resolve(entity.getUrl()).normalize();

        if (!filePath.startsWith(UPLOADS_DIR)) {
            throw new SecurityException("Invalid file path");
        }

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete attachment: " + filePath, e);
        }

        attachmentRepository.delete(entity);
    }
}
