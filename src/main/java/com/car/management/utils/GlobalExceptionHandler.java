package com.car.management.utils;

import com.car.management.utils.default_exceptions.DatabaseConnectionException;
import com.car.management.utils.default_exceptions.EntityAlreadyExistsException;
import com.car.management.utils.default_exceptions.EntityNotFoundException;
import com.car.management.utils.default_exceptions.ServiceUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleAllServiceExceptions(RuntimeException ex, WebRequest request) {

        return switch (ex) {
            case EntityNotFoundException e ->
                    createResponse(HttpStatus.NOT_FOUND, "Resource Not Found", e.getMessage());

            case EntityAlreadyExistsException e ->
                    createResponse(HttpStatus.CONFLICT, "Duplicate Entity", e.getMessage());

            case DatabaseConnectionException e ->
                    createResponse(HttpStatus.SERVICE_UNAVAILABLE, "Database Error", "Problem z połączeniem do bazy danych");

            case ServiceUnavailableException e ->
                    createResponse(HttpStatus.BAD_GATEWAY, "External Service Error", e.getMessage());

            default ->
                    createResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error", "Wystąpił nieoczekiwany błąd");
        };
    }

    private ResponseEntity<Object> createResponse(HttpStatus status, String title, String detail) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setProperty("timestamp", Instant.now());
        // Możesz dodać link do dokumentacji błędów
        problemDetail.setType(URI.create("https://api.twoja-apka.pl/errors/" + status.value()));

        return ResponseEntity.status(status).body(problemDetail);
    }
}