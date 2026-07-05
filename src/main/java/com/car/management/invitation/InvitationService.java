package com.car.management.invitation;

import com.car.management.users.models.UserEntity;
import com.car.management.users.repository.UserRepository;
import com.car.management.utils.mailing.MailService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvitationService {

    CarInvitationRepository invitationRepository;
    CarAccessRepository carAccessRepository;
    UserRepository userRepository;
    MailService mailService;

    public void createInvitations(Long carId, String carName, List<String> emails) {
        emails.forEach(email -> {
            String token = UUID.randomUUID().toString();

            CarInvitationEntity invitation = CarInvitationEntity.builder()
                    .token(token)
                    .carId(carId)
                    .carName(carName)
                    .inviteeEmail(email)
                    .status(InvitationStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .expiresAt(LocalDateTime.now().plusDays(7))
                    .build();

            invitationRepository.save(invitation);
            mailService.sendInvitation(email, carName, token);
        });
    }

    public InvitationDto getByToken(String token) {
        CarInvitationEntity inv = invitationRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Invitation not found"));
        return new InvitationDto(inv.getInvitationId(), inv.getCarId(), inv.getCarName(), inv.getInviteeEmail(), inv.getStatus());
    }

    public List<InvitationDto> getByCarId(Long carId) {
        return invitationRepository.findAllByCarId(carId).stream()
                .map(inv -> new InvitationDto(inv.getInvitationId(), inv.getCarId(), inv.getCarName(), inv.getInviteeEmail(), inv.getStatus()))
                .toList();
    }

    @Transactional
    public void resendInvitation(Long invitationId) {
        CarInvitationEntity inv = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new EntityNotFoundException("Invitation not found"));

        String newToken = UUID.randomUUID().toString();
        inv.setToken(newToken);
        inv.setExpiresAt(LocalDateTime.now().plusDays(7));
        inv.setStatus(InvitationStatus.PENDING);
        invitationRepository.save(inv);

        mailService.sendInvitation(inv.getInviteeEmail(), inv.getCarName(), newToken);
    }

    @Transactional
    public void acceptInvitation(String token) {
        CarInvitationEntity inv = invitationRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Invitation not found"));

        if (inv.getStatus() == InvitationStatus.ACCEPTED) return;
        if (inv.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Invitation has expired");
        }

        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        boolean alreadyHasAccess = carAccessRepository
                .findAllByUserId(user.getUserId())
                .stream()
                .anyMatch(a -> a.getCarId().equals(inv.getCarId()));

        if (!alreadyHasAccess) {
            carAccessRepository.save(CarAccessEntity.builder()
                    .carId(inv.getCarId())
                    .userId(user.getUserId())
                    .build());
        }

        inv.setStatus(InvitationStatus.ACCEPTED);
        invitationRepository.save(inv);
    }
}
