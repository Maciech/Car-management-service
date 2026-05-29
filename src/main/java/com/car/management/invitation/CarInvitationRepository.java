package com.car.management.invitation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarInvitationRepository extends JpaRepository<CarInvitationEntity, Long> {

    Optional<CarInvitationEntity> findByToken(String token);
}
