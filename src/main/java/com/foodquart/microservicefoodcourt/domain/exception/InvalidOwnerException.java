package com.foodquart.microservicefoodcourt.domain.exception;

public class InvalidOwnerException extends DomainException {
    public InvalidOwnerException(Long ownerId, Long restaurantId) {
        super("User with ID " + ownerId + " is not the owner of restaurant with ID " + restaurantId);
    }
}
