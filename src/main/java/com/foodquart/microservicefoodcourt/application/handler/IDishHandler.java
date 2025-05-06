package com.foodquart.microservicefoodcourt.application.handler;

import com.foodquart.microservicefoodcourt.application.dto.*;
import org.springframework.data.domain.Page;

public interface IDishHandler {
    DishResponseDto createDish(DishRequestDto dishRequestDto, Long ownerId);

    DishResponseDto updateDish(UpdateDishRequestDto updateDishRequestDto, Long ownerId);

    DishResponseDto enableOrDisableDish(EnableDishRequestDto enableDishRequestDto, Long ownerId);

    Page<DishListResponseDto> getDishesByRestaurant(Long restaurantId, String category, int page, int size);
}
