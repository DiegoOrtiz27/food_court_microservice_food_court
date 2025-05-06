package com.foodquart.microservicefoodcourt.application.handler;

import com.foodquart.microservicefoodcourt.application.dto.DishRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.DishResponseDto;
import com.foodquart.microservicefoodcourt.application.dto.EnableDishRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.UpdateDishRequestDto;

public interface IDishHandler {
    DishResponseDto createDish(DishRequestDto dishRequestDto, Long ownerId);

    DishResponseDto updateDish(UpdateDishRequestDto updateDishRequestDto, Long ownerId);

    DishResponseDto enableOrDisableDish(EnableDishRequestDto enableDishRequestDto, Long ownerId);
}
