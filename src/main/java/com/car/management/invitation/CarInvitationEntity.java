package com.car.management.invitation;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "car_invitation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarInvitationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long invitationId;

    @Column(unique = true, nullable = false)
    String token;

    Long carId;
    String carName;
    String inviteeEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    InvitationStatus status;

    @Column(nullable = false)
    LocalDateTime createdAt;

    @Column(nullable = false)
    LocalDateTime expiresAt;
}
