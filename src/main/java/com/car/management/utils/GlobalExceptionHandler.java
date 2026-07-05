package com.car.management.utils;

import com.car.management.utils.default_exceptions.DatabaseConnectionException;
import com.car.management.utils.default_exceptions.EmailOrPasswordIncorrectException;
import com.car.management.utils.default_exceptions.EntityAlreadyExistsException;
import com.car.management.utils.default_exceptions.EntityNotFoundException;
import com.car.management.utils.default_exceptions.ServiceUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /** Loguje błąd deserializacji JSON (400) i zwraca czytelny komunikat */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        log.error("Błąd deserializacji JSON (400): {}", ex.getMessage(), ex);
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Nieprawidłowy format danych: " + ex.getMostSpecificCause().getMessage());
        pd.setTitle("Bad Request");
        pd.setProperty("timestamp", Instant.now());
        return ResponseEntity.badRequest().body(pd);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleAllServiceExceptions(RuntimeException ex, WebRequest request) {
        return switch (ex) {
            case EntityNotFoundException e ->
                    createResponse(HttpStatus.NOT_FOUND, "Resource Not Found", e.getMessage());

            case EmailOrPasswordIncorrectException e ->
                    createResponse(HttpStatus.UNAUTHORIZED, "Authentication Failed", e.getMessage());

            case EntityAlreadyExistsException e ->
                    createResponse(HttpStatus.CONFLICT, "Duplicate Entity", e.getMessage());

            case DatabaseConnectionException e ->
                    createResponse(HttpStatus.SERVICE_UNAVAILABLE, "Database Error", "Problem z połączeniem do bazy danych");

            case ServiceUnavailableException e ->
                    createResponse(HttpStatus.BAD_GATEWAY, "External Service Error", e.getMessage());

            case MailException e -> {
                log.error("Błąd wysyłki e-mail: {}", e.getMessage(), e);
                yield createResponse(HttpStatus.BAD_GATEWAY, "Mail Error",
                        "Nie udało się wysłać wiadomości e-mail. Sprawdź konfigurację SMTP.");
            }

            default -> {
                log.error("Nieobsłużony wyjątek [{}]: {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
                yield createResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error", "Wystąpił nieoczekiwany błąd");
            }
        };
    }

    private ResponseEntity<Object> createResponse(HttpStatus status, String title, String detail) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title);
        pd.setProperty("timestamp", Instant.now());
        pd.setType(URI.create("https://api.twoja-apka.pl/errors/" + status.value()));
        return ResponseEntity.status(status).body(pd);
    }
}
