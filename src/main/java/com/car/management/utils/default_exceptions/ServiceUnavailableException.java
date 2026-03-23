package com.car.management.utils.default_exceptions;

public final class ServiceUnavailableException extends RuntimeException implements ServiceException {
    public ServiceUnavailableException(String serviceName) {
        super("Serwis zewnętrzny %s jest obecnie nieosiągalny".formatted(serviceName));
    }
}
