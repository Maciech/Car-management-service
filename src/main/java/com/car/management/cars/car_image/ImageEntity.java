package com.car.management.cars.car_image;

import com.car.management.utils.DefaultDatabaseFields;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class ImageEntity extends DefaultDatabaseFields {

    @Id
    @GeneratedValue
    private Long imageId;

    private Long carId;

    private String url;

    private int position;
}
