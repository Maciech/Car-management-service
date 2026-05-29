package com.car.management.invitation;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/invite")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvitationController {

    InvitationService invitationService;

    /** Public — no auth needed, just the token */
    @GetMapping
    public ResponseEntity<InvitationDto> getInvitation(@RequestParam String token) {
        return ResponseEntity.ok(invitationService.getByToken(token));
    }

    /** Protected — current logged-in user accepts */
    @PostMapping("/accept")
    public ResponseEntity<?> acceptInvitation(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        invitationService.acceptInvitation(token);
        return ResponseEntity.ok().build();
    }
}
