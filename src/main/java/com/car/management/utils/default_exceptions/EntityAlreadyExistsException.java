package com.car.management.utils.default_exceptions;

public final class EntityAlreadyExistsException extends RuntimeException implements ServiceException {
    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}
