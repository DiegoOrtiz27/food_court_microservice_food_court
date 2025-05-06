package com.foodquart.microservicefoodcourt.application.handler.impl;

import com.foodquart.microservicefoodcourt.application.dto.CreateRestaurantEmployeeRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.CreateRestaurantEmployeeResponseDto;
import com.foodquart.microservicefoodcourt.application.handler.IRestaurantEmployeeHandler;
import com.foodquart.microservicefoodcourt.application.mapper.IRestaurantEmployeeRequestMapper;
import com.foodquart.microservicefoodcourt.domain.api.IRestaurantEmployeeServicePort;
import com.foodquart.microservicefoodcourt.domain.model.RestaurantEmployeeModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantEmployeeHandler implements IRestaurantEmployeeHandler {

    private final IRestaurantEmployeeServicePort restaurantEmployeeServicePort;
    private final IRestaurantEmployeeRequestMapper restaurantEmployeeRequestMapper;
    @Override
    public CreateRestaurantEmployeeResponseDto addEmployeeToRestaurant(Long ownerId, CreateRestaurantEmployeeRequestDto createRestaurantEmployeeRequestDto) {
        RestaurantEmployeeModel restaurantEmployeeModel = restaurantEmployeeRequestMapper.toRestaurantEmployee(createRestaurantEmployeeRequestDto);
        restaurantEmployeeModel = restaurantEmployeeServicePort.addEmployeeToRestaurant(ownerId, restaurantEmployeeModel);
        return restaurantEmployeeRequestMapper.toResponse("The employee has been created successfully", restaurantEmployeeModel.getEmployeeId());
    }
}
