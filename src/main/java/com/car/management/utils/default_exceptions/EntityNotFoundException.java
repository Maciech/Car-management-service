package com.car.management.utils.default_exceptions;

// Konkretne implementacje jako rekordy lub klasy
public final class EntityNotFoundException extends RuntimeException implements ServiceException {
    public EntityNotFoundException(String entity, Object id) {
        super("%s o identyfikatorze %s nie został znaleziony".formatted(entity, id));
    }
}
