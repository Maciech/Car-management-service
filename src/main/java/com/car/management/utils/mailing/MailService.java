package com.car.management.utils.mailing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend.url:http://localhost:4200}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String fromAddress;

    public void sendInvitation(String toEmail, String carName, String token) {
        String link = frontendUrl + "/invite?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toEmail);
        message.setSubject("Zaproszenie do zarządzania pojazdem – " + carName);
        message.setText(
                "Dzień dobry,\n\n" +
                "Zostałeś zaproszony do zarządzania kosztami pojazdu: " + carName + "\n\n" +
                "Kliknij link, aby przyjąć zaproszenie:\n" + link + "\n\n" +
                "Jeśli nie masz jeszcze konta, możesz je założyć pod tym samym adresem.\n" +
                "Link wygasa za 7 dni.\n\n" +
                "Pozdrawiamy,\nAutoFleet"
        );

        try {
            log.info("Wysyłam zaproszenie na adres: {}", toEmail);
            mailSender.send(message);
            log.info("Zaproszenie wysłane pomyślnie → {}", toEmail);
        } catch (MailException e) {
            log.error("Nie udało się wysłać maila do {} — {}: {}", toEmail, e.getClass().getSimpleName(), e.getMessage(), e);
            // rzucamy dalej żeby GlobalExceptionHandler mógł zwrócić 502
            throw e;
        }
    }
}
