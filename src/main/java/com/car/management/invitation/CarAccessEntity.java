package com.car.management.invitation;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "car_access",
       uniqueConstraints = @UniqueConstraint(columnNames = {"car_id", "user_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarAccessEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long accessId;

    @Column(name = "car_id", nullable = false)
    Long carId;

    @Column(name = "user_id", nullable = false)
    Long userId;
}
