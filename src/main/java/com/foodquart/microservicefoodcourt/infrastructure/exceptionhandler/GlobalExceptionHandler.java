package com.foodquart.microservicefoodcourt.infrastructure.exceptionhandler;

import com.foodquart.microservicefoodcourt.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidOwnerRoleException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOwnerRole(InvalidOwnerRoleException ex) {
        ErrorResponse error = new ErrorResponse("INVALID_OWNER_ROLE", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
        ErrorResponse error = new ErrorResponse("DOMAIN_ERROR", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NitAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateNitException(NitAlreadyExistsException ex) {
        ErrorResponse error = new ErrorResponse("DUPLICATE_NIT", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidRestaurantException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRestaurant(InvalidRestaurantException ex) {
        ErrorResponse error = new ErrorResponse("INVALID_RESTAURANT", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidOwnerException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOwnerRestaurant(InvalidOwnerException ex) {
        ErrorResponse error = new ErrorResponse("INVALID_OWNER_RESTAURANT", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDishException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDish(InvalidDishException ex) {
        ErrorResponse error = new ErrorResponse("INVALID_DISH", ex.getMessage());
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse("INTERNAL_SERVER_ERROR", "An unexpected error occurred.");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}