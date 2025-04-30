package com.foodquart.microservicefoodcourt.application.handler;

import com.foodquart.microservicefoodcourt.application.request.DishRequestDto;

public interface IDishHandler {
    void createDish(DishRequestDto dishRequestDto, Long ownerId);
}
