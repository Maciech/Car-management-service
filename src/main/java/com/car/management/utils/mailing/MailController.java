package com.car.management.utils.mailing;

import com.car.management.invitation.InvitationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.car.management.utils.Constants.MAIL_SENDER_API;

@RestController
@RequestMapping(MAIL_SENDER_API)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailController {

    InvitationService invitationService;

    @PostMapping("/invite")
    public ResponseEntity<?> inviteToCar(@RequestBody InviteRequest request) {
        invitationService.createInvitations(request.carId(), request.carName(), request.emails());
        return ResponseEntity.accepted().build();
    }

    public record InviteRequest(Long carId, String carName, List<String> emails) {}
}
