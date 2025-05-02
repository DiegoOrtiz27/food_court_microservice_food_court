package com.foodquart.microservicefoodcourt.domain.exception;

public class InvalidDishException extends DomainException {
    public InvalidDishException(Long dishId) {
        super("Dish with id '" + dishId + "' not found");
    }
}
