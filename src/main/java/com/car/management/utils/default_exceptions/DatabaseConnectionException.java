package com.car.management.utils.default_exceptions;

public final class DatabaseConnectionException extends RuntimeException implements ServiceException {
    public DatabaseConnectionException(String message) {
        super(message);
    }
}
