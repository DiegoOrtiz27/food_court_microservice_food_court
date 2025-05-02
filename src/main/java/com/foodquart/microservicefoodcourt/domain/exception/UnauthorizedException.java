package com.foodquart.microservicefoodcourt.domain.exception;

public class UnauthorizedException extends DomainException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
