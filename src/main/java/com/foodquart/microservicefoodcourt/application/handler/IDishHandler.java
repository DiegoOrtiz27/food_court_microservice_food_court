package com.foodquart.microservicefoodcourt.application.handler;

import com.foodquart.microservicefoodcourt.application.dto.DishRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.UpdateDishRequestDto;

public interface IDishHandler {
    void createDish(DishRequestDto dishRequestDto, Long ownerId);

    void updateDish(UpdateDishRequestDto updateDishRequestDto, Long ownerId);
}
