package com.car.management.utils.default_exceptions;

public final class EmailOrPasswordIncorrectException extends RuntimeException implements ServiceException {
    public EmailOrPasswordIncorrectException() {
        super("Email lub hasło jest nieprawidłowe");
    }
}
