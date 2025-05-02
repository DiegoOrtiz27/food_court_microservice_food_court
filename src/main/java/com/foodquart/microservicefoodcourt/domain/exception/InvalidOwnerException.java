package com.foodquart.microservicefoodcourt.domain.exception;

public class InvalidOwnerException extends DomainException {
    public InvalidOwnerException(Long restaurantId) {
        super("User is not the owner of restaurant with ID " + restaurantId);
    }
}
