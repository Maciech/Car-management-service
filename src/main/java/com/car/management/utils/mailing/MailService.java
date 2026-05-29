package com.car.management.utils.mailing;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailService {

    JavaMailSender mailSender;

    @Value("${app.frontend.url:http://localhost:4200}")
    String frontendUrl;

    public void sendInvitation(String email, String carName, String token) {
        String link = frontendUrl + "/invite?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("car.base.pl@gmail.com");
        message.setTo(email);
        message.setSubject("Zaproszenie do zarządzania pojazdem – " + carName);
        message.setText(
                "Dzień dobry,\n\n" +
                "Zostałeś zaproszony do zarządzania kosztami pojazdu: " + carName + "\n\n" +
                "Kliknij link, aby przyjąć zaproszenie:\n" + link + "\n\n" +
                "Jeśli nie masz jeszcze konta, możesz je założyć pod tym samym adresem.\n" +
                "Link wygasa za 7 dni.\n\n" +
                "Pozdrawiamy,\nCar Management"
        );

        mailSender.send(message);
    }
}
