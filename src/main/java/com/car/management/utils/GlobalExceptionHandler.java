package com.car.management.utils;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<ApiErrorResponse> handleNotFound(ChangeSetPersister.NotFoundException ex) {
//        return ResponseEntity
//                .status(HttpStatus.NOT_FOUND)
//                .body(ApiErrorResponse.of(
//                        HttpStatus.NOT_FOUND,
//                        ex.getMessage()
//                ));
//    }
//
//    @ExceptionHandler(AccessDeniedAppException.class)
//    public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedAppException ex) {
//        return ResponseEntity
//                .status(HttpStatus.FORBIDDEN)
//                .body(ApiErrorResponse.of(
//                        HttpStatus.FORBIDDEN,
//                        ex.getMessage()
//                ));
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
//        return ResponseEntity
//                .badRequest()
//                .body(ApiErrorResponse.fromValidation(ex));
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiErrorResponse> handleFallback(Exception ex) {
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(ApiErrorResponse.of(
//                        HttpStatus.INTERNAL_SERVER_ERROR,
//                        "Unexpected error"
//                ));
//    }
}

