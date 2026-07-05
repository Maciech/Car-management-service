package com.car.management.cars.car_attachment;

import com.car.management.utils.DefaultDatabaseFields;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class AttachmentEntity extends DefaultDatabaseFields {

    @Id
    @GeneratedValue
    private Long attachmentId;

    private Long carId;

    private String url;

    private String originalName;

    private String mimeType;
}
