package com.car.management.invitation;

public record InvitationDto(
        Long carId,
        String carName,
        String inviteeEmail,
        InvitationStatus status
) {}
