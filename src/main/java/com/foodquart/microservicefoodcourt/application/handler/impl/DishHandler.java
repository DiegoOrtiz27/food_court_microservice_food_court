package com.foodquart.microservicefoodcourt.application.handler.impl;

import com.foodquart.microservicefoodcourt.application.handler.IDishHandler;
import com.foodquart.microservicefoodcourt.application.mapper.IDishRequestMapper;
import com.foodquart.microservicefoodcourt.application.dto.DishRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.UpdateDishRequestDto;
import com.foodquart.microservicefoodcourt.domain.api.IDishServicePort;
import com.foodquart.microservicefoodcourt.domain.model.DishModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class DishHandler implements IDishHandler {

    private final IDishServicePort dishServicePort;
    private final IDishRequestMapper dishRequestMapper;

    @Override
    public void createDish(DishRequestDto dishRequestDto) {
        DishModel dishModel = dishRequestMapper.toDish(dishRequestDto);
        dishServicePort.createDish(dishModel);

    }

    @Override
    public void updateDish(UpdateDishRequestDto updateDishRequestDto) {
        DishModel dishModel = dishRequestMapper.toDish(updateDishRequestDto);
        dishServicePort.updateDish(dishModel);
    }
}