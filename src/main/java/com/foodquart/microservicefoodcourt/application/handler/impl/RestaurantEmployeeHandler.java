package com.foodquart.microservicefoodcourt.application.handler.impl;

import com.foodquart.microservicefoodcourt.application.dto.request.CreateRestaurantEmployeeRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.response.CreateRestaurantEmployeeResponseDto;
import com.foodquart.microservicefoodcourt.application.handler.IRestaurantEmployeeHandler;
import com.foodquart.microservicefoodcourt.application.mapper.request.IRestaurantEmployeeRequestMapper;
import com.foodquart.microservicefoodcourt.application.mapper.response.IRestaurantEmployeeResponseMapper;
import com.foodquart.microservicefoodcourt.domain.api.IRestaurantEmployeeServicePort;
import com.foodquart.microservicefoodcourt.domain.model.RestaurantEmployeeModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.foodquart.microservicefoodcourt.domain.util.RestaurantMessages.EMPLOYEE_CREATED;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantEmployeeHandler implements IRestaurantEmployeeHandler {

    private final IRestaurantEmployeeServicePort restaurantEmployeeServicePort;
    private final IRestaurantEmployeeRequestMapper restaurantEmployeeRequestMapper;
    private final IRestaurantEmployeeResponseMapper restaurantEmployeeResponseMapper;

    @Override
    public CreateRestaurantEmployeeResponseDto addEmployeeToRestaurant(Long restaurantId, CreateRestaurantEmployeeRequestDto createRestaurantEmployeeRequestDto) {
        RestaurantEmployeeModel restaurantEmployeeModel = restaurantEmployeeRequestMapper.toRestaurantEmployee(createRestaurantEmployeeRequestDto);
        restaurantEmployeeModel = restaurantEmployeeServicePort.addEmployeeToRestaurant(restaurantId, restaurantEmployeeModel);
        return restaurantEmployeeResponseMapper.toResponse(restaurantEmployeeModel.getEmployeeId(), EMPLOYEE_CREATED);
    }
}
