package com.foodquart.microservicefoodcourt.application.handler.impl;

import com.foodquart.microservicefoodcourt.application.dto.*;
import com.foodquart.microservicefoodcourt.application.handler.IDishHandler;
import com.foodquart.microservicefoodcourt.application.mapper.IDishRequestMapper;
import com.foodquart.microservicefoodcourt.application.mapper.IDishResponseMapper;
import com.foodquart.microservicefoodcourt.domain.api.IDishServicePort;
import com.foodquart.microservicefoodcourt.domain.model.DishModel;
import com.foodquart.microservicefoodcourt.domain.util.DishMessages;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class DishHandler implements IDishHandler {

    private final IDishServicePort dishServicePort;
    private final IDishRequestMapper dishRequestMapper;
    private final IDishResponseMapper dishResponseMapper;

    @Override
    public DishResponseDto createDish(DishRequestDto dishRequestDto, Long ownerId) {
        DishModel dishModel = dishRequestMapper.toDish(dishRequestDto);
        dishModel = dishServicePort.createDish(dishModel, ownerId);
        return dishResponseMapper.toResponse(dishModel.getId(), DishMessages.DISH_CREATED);
    }

    @Override
    public DishResponseDto updateDish(UpdateDishRequestDto updateDishRequestDto, Long ownerId) {
        DishModel dishModel = dishRequestMapper.toDish(updateDishRequestDto);
        dishServicePort.updateDish(dishModel, ownerId);
        return dishResponseMapper.toResponse(dishModel.getId(), DishMessages.DISH_UPDATED);
    }

    @Override
    public DishResponseDto enableOrDisableDish(EnableDishRequestDto enableDishRequestDto, Long ownerId) {
        DishModel dishModel = dishRequestMapper.toDish(enableDishRequestDto);
        dishServicePort.enableOrDisableDish(dishModel, ownerId);
        String response = Boolean.TRUE.equals(enableDishRequestDto.getActive())
                ? DishMessages.DISH_ENABLED
                : DishMessages.DISH_DISABLED;
        return dishResponseMapper.toResponse(dishModel.getId(), response);
    }

    @Override
    public Page<DishListResponseDto> getDishesByRestaurant(Long restaurantId, String category, int page, int size) {
        Page<DishModel> dishModel = dishServicePort.getDishesByRestaurant(restaurantId, category, page, size);
        return dishModel.map(dishResponseMapper::toResponse);
    }
}