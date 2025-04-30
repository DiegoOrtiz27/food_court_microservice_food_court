package com.foodquart.microservicefoodcourt.domain.api;

public interface IUpdateDishServicePort {
    void updateDish(Long dishId, String description, Integer price, Long ownerId);
}
