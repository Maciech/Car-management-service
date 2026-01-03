package com.car.management.cars.car_image;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageController {

    ImageService imageService;

    @PostMapping("/images/{carId}")
    public ResponseEntity<?> uploadImages(@PathVariable Long carId,
                                          @RequestParam("files") List<MultipartFile> files) throws IOException {
        imageService.saveImagesByCarId(carId, files);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/images/{carId}")
    public ResponseEntity<@NonNull List<ImageDto>> getImages(@PathVariable Long carId) {
        return ResponseEntity.ok().body(imageService.getImagesByCarId(carId));
    }

    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<?> deleteImageByImageId(@PathVariable Long imageId) {
        imageService.deleteImageByImageId(imageId);
        return ResponseEntity.ok().build();
    }
}
