package com.foodquart.microservicefoodcourt.domain.exception;

public class NoDataFoundException extends DomainException {
    public NoDataFoundException(String message) {
        super(message);
    }
}
