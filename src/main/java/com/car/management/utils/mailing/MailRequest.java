package com.car.management.utils.mailing;

public record MailRequest(
        String to,
        String subject,
        String message
) {}
