package com.foodquart.microservicefoodcourt.application.handler;

import com.foodquart.microservicefoodcourt.application.request.DishRequestDto;
import com.foodquart.microservicefoodcourt.application.request.DishUpdateRequestDto;

public interface IDishHandler {
    void createDish(DishRequestDto dto, Long ownerId);

    void updateDish(Long dishId, DishUpdateRequestDto dto, Long ownerId);
}
