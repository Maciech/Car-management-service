package com.car.management.cars.car_attachment;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttachmentController {

    AttachmentService attachmentService;

    @PostMapping("/attachments/{carId}")
    public ResponseEntity<?> uploadAttachments(@PathVariable Long carId,
                                               @RequestParam("files") List<MultipartFile> files) {
        attachmentService.saveAttachmentsByCarId(carId, files);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/attachments/{carId}")
    public ResponseEntity<@NonNull List<AttachmentDto>> getAttachments(@PathVariable Long carId) {
        return ResponseEntity.ok(attachmentService.getAttachmentsByCarId(carId));
    }

    @DeleteMapping("/attachments/{attachmentId}")
    public ResponseEntity<?> deleteAttachment(@PathVariable Long attachmentId) {
        attachmentService.deleteAttachment(attachmentId);
        return ResponseEntity.ok().build();
    }
}
