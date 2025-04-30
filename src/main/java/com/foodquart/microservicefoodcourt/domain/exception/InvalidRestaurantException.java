package com.foodquart.microservicefoodcourt.domain.exception;

public class InvalidRestaurantException extends DomainException {
    public InvalidRestaurantException(Long id) {
        super("The restaurant with id '" + id + "' does not exists");
    }
}
