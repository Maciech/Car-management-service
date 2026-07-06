package com.car.management.cars.car_attachment;

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
public class AttachmentService {

    AttachmentRepository attachmentRepository;
    CarRepository carRepository;
    ModelMapper modelMapper;

    @NonFinal
    @Value("${app.backend.url:http://localhost:8080}")
    String backendUrl;

    private static final Path UPLOADS_DIR = Paths.get("uploads");

    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
            "image/jpeg", "image/png", "image/gif", "image/webp", "application/pdf"
    );

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
            validateAttachmentFile(file);

            String safeName = Paths.get(file.getOriginalFilename() != null ? file.getOriginalFilename() : "file").getFileName().toString();
            String filename = UUID.randomUUID() + "-" + safeName;
            Path target = carDir.resolve(filename).normalize();

            if (!target.startsWith(UPLOADS_DIR)) {
                throw new SecurityException("Invalid file path");
            }

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
                    dto.setUrl(backendUrl + "/uploads/" + a.getUrl());
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

    private void validateAttachmentFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("File type not allowed: " + contentType);
        }
        try (InputStream is = file.getInputStream()) {
            byte[] header = new byte[8];
            if (is.read(header) < 4) throw new IllegalArgumentException("File too small");
            if (!hasAllowedMagicBytes(header, contentType)) throw new IllegalArgumentException("File content does not match declared type");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }

    private boolean hasAllowedMagicBytes(byte[] h, String contentType) {
        // PDF: 25 50 44 46
        if (contentType.equals("application/pdf")) {
            return h[0] == 0x25 && h[1] == 0x50 && h[2] == 0x44 && h[3] == 0x46;
        }
        // JPEG: FF D8 FF
        if (h[0] == (byte) 0xFF && h[1] == (byte) 0xD8 && h[2] == (byte) 0xFF) return true;
        // PNG: 89 50 4E 47
        if (h[0] == (byte) 0x89 && h[1] == 0x50 && h[2] == 0x4E && h[3] == 0x47) return true;
        // GIF: 47 49 46 38
        if (h[0] == 0x47 && h[1] == 0x49 && h[2] == 0x46 && h[3] == 0x38) return true;
        // WebP: RIFF
        if (h[0] == 0x52 && h[1] == 0x49 && h[2] == 0x46 && h[3] == 0x46) return true;
        return false;
    }
}
