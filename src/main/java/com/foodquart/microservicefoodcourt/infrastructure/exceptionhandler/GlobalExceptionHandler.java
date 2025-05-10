package com.foodquart.microservicefoodcourt.infrastructure.exceptionhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodquart.microservicefoodcourt.domain.exception.*;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
        ErrorResponse error = new ErrorResponse("DOMAIN_ERROR", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoDataFoundException(NoDataFoundException ex) {
        ErrorResponse error = new ErrorResponse("NO_DATA_FOUND", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleNoDataFoundException(UnauthorizedException ex) {
        ErrorResponse error = new ErrorResponse("UNAUTHORIZED", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("An unexpected error occurred: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse("INTERNAL_SERVER_ERROR", "An unexpected error occurred.");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<ValidationError> errors = fieldErrors.stream()
                .map(error -> new ValidationError(
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getRejectedValue()))
                .toList();

        ValidationErrorResponse response = new ValidationErrorResponse(
                "VALIDATION_FAILED",
                "Validation failed for one or more fields",
                errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(FeignException ex) {
        Optional<ErrorResponse> errorResponseOptional = parseFeignError(ex);
        if (errorResponseOptional.isPresent()) {
            return new ResponseEntity<>(errorResponseOptional.get(), HttpStatus.CONFLICT);
        }
        ErrorResponse error = new ErrorResponse("INTERNAL_SERVER_ERROR", "Error communicating with another service.");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Optional<ErrorResponse> parseFeignError(FeignException ex) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return ex.responseBody()
                    .flatMap(responseBody -> {
                        try {
                            String errorBody = new String(responseBody.array());
                            return Optional.ofNullable(objectMapper.readValue(errorBody, ErrorResponse.class));
                        } catch (IOException e) {
                            return Optional.empty();
                        }
                    });
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}