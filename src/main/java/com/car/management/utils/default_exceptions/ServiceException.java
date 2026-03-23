package com.car.management.utils.default_exceptions;


public sealed interface ServiceException
        permits EntityNotFoundException, EntityAlreadyExistsException, DatabaseConnectionException, ServiceUnavailableException,
        EmailOrPasswordIncorrectException {
    String getMessage();
}

