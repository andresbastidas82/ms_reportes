package com.pragma.ms_reportes.infrastructure.exception;

import com.pragma.ms_reportes.domain.exception.BootcampAlreadyExistsException;
import com.pragma.ms_reportes.domain.exception.InvalidBootcampException;
import com.pragma.ms_reportes.domain.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BootcampAlreadyExistsException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleBootcampAlreadyExistsException(
            BootcampAlreadyExistsException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .code(HttpStatus.CONFLICT.value())
                .errors(List.of(ex.getMessage()))
                .timestamp(LocalDateTime.now()).build();

        return buildResponse(HttpStatus.CONFLICT, error);
    }

    @ExceptionHandler(InvalidBootcampException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleInvalidBootcampException(
            InvalidBootcampException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .errors(List.of(ex.getMessage()))
                .timestamp(LocalDateTime.now()).build();

        return buildResponse(HttpStatus.BAD_REQUEST, error);
    }

    @ExceptionHandler(NotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleNotFoundException(
            NotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .errors(List.of(ex.getMessage()))
                .timestamp(LocalDateTime.now()).build();

        return buildResponse(HttpStatus.NOT_FOUND, error);
    }

    private Mono<ResponseEntity<ErrorResponse>> buildResponse(HttpStatus status, ErrorResponse error) {
        return Mono.just(ResponseEntity
                .status(status)
                .body(error)
        );
    }
}
