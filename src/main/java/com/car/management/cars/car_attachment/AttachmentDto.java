package com.car.management.cars.car_attachment;

import lombok.Data;

@Data
public class AttachmentDto {
    private Long attachmentId;
    private Long carId;
    private String url;
    private String originalName;
    private String mimeType;
}
