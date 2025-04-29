package com.foodquart.microservicefoodcourt.domain.exception;

public class InvalidOwnerRoleException extends RuntimeException {
    public InvalidOwnerRoleException(Long ownerId) {
        super("User with ID " + ownerId + " does not have OWNER role.");
    }
}
