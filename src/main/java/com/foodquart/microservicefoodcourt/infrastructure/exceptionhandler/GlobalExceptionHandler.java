package com.foodquart.microservicefoodcourt.infrastructure.exceptionhandler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.foodquart.microservicefoodcourt.domain.exception.*;
import com.foodquart.microservicefoodcourt.domain.util.HttpMessages;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    public static final String DOMAIN_ERROR_CODE = "DOMAIN_ERROR";
    public static final String NO_DATA_FOUND_CODE = "NO_DATA_FOUND";
    public static final String UNAUTHORIZED_CODE = "UNAUTHORIZED";
    public static final String VALIDATION_FAILED_CODE = "VALIDATION_FAILED";
    public static final String INTERNAL_SERVER_ERROR_CODE = "INTERNAL_SERVER_ERROR";
    public static final String INVALID_REQUEST_BODY_CODE = "INVALID_REQUEST_BODY";
    public static final String UNKNOWN_FIELD_CODE = "UNKNOWN_FIELD";
    public static final String INVALID_REQUEST_FORMAT_CODE = "INVALID_REQUEST_FORMAT";
    public static final String INVALID_JSON_CODE = "INVALID_JSON";

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
        return buildErrorResponse(DOMAIN_ERROR_CODE, ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoDataFoundException(NoDataFoundException ex) {
        return buildErrorResponse(NO_DATA_FOUND_CODE, ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
        return buildErrorResponse(UNAUTHORIZED_CODE, ex.getMessage(), HttpStatus.UNAUTHORIZED);
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
                VALIDATION_FAILED_CODE,
                HttpMessages.VALIDATION_FAILED,
                errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(FeignException ex) {
        Optional<ErrorResponse> errorResponseOptional = parseFeignError(ex);
        return errorResponseOptional.map(errorResponse -> new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT)).orElseGet(() -> buildErrorResponse(INTERNAL_SERVER_ERROR_CODE,
                HttpMessages.SERVICE_COMMUNICATION_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        if (ex.getRootCause() instanceof UnrecognizedPropertyException unrecognizedEx) {
            return handleUnrecognizedPropertyException(unrecognizedEx);
        }
        return buildErrorResponse(INVALID_REQUEST_BODY_CODE,
                HttpMessages.INVALID_REQUEST_BODY, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnrecognizedPropertyException.class)
    public ResponseEntity<ErrorResponse> handleUnrecognizedPropertyException(UnrecognizedPropertyException ex) {
        String message = String.format(HttpMessages.UNKNOWN_FIELD,
                ex.getPropertyName(), ex.getKnownPropertyIds());
        return buildErrorResponse(UNKNOWN_FIELD_CODE, message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<ErrorResponse> handleJsonMappingException(JsonMappingException ex) {
        return buildErrorResponse(INVALID_REQUEST_FORMAT_CODE,
                HttpMessages.INVALID_REQUEST_FORMAT, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<ErrorResponse> handleJsonParseException(JsonParseException ex) {
        return buildErrorResponse(INVALID_JSON_CODE,
                HttpMessages.INVALID_JSON, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error(HttpMessages.UNEXPECTED_ERROR, ex);
        return buildErrorResponse(INTERNAL_SERVER_ERROR_CODE,
                HttpMessages.UNEXPECTED_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
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

    private ResponseEntity<ErrorResponse> buildErrorResponse(String code, String message, HttpStatus status) {
        return new ResponseEntity<>(new ErrorResponse(code, message), status);
    }
}