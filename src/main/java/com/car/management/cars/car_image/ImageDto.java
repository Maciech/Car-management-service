package com.car.management.cars.car_image;

import com.car.management.utils.DefaultDatabaseFields;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ImageDto extends DefaultDatabaseFields {

    private Long imageId;

    private Long carId;

    private String url;

    private int position;
}
