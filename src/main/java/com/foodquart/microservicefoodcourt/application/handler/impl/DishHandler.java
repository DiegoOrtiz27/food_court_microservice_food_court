package com.foodquart.microservicefoodcourt.application.handler.impl;

import com.foodquart.microservicefoodcourt.application.handler.IDishHandler;
import com.foodquart.microservicefoodcourt.application.mapper.IDishRequestMapper;
import com.foodquart.microservicefoodcourt.application.request.DishRequestDto;
import com.foodquart.microservicefoodcourt.application.request.DishUpdateRequestDto;
import com.foodquart.microservicefoodcourt.domain.api.ICreateDishServicePort;
import com.foodquart.microservicefoodcourt.domain.api.IUpdateDishServicePort;
import com.foodquart.microservicefoodcourt.domain.model.DishModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class DishHandler implements IDishHandler {

    private final ICreateDishServicePort dishServicePort;
    private final IUpdateDishServicePort updateDishServicePort;
    private final IDishRequestMapper dishRequestMapper;

    @Override
    public void createDish(DishRequestDto dto, Long ownerId) {
        DishModel dishModel = dishRequestMapper.toDish(dto);
        dishServicePort.createDish(dishModel, ownerId);

    }

    @Override
    public void updateDish(Long dishId, DishUpdateRequestDto dto, Long ownerId) {
        updateDishServicePort.updateDish(dishId, dto.getDescription(), dto.getPrice(), ownerId);
    }
}