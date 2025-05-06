package com.foodquart.microservicefoodcourt.application.handler.impl;

import com.foodquart.microservicefoodcourt.application.dto.RestaurantListResponseDto;
import com.foodquart.microservicefoodcourt.application.dto.RestaurantResponseDto;
import com.foodquart.microservicefoodcourt.application.handler.IRestaurantHandler;
import com.foodquart.microservicefoodcourt.application.mapper.IRestaurantRequestMapper;
import com.foodquart.microservicefoodcourt.application.dto.RestaurantRequestDto;
import com.foodquart.microservicefoodcourt.application.mapper.IRestaurantResponseMapper;
import com.foodquart.microservicefoodcourt.domain.api.IRestaurantServicePort;
import com.foodquart.microservicefoodcourt.domain.model.RestaurantModel;
import com.foodquart.microservicefoodcourt.domain.util.RestaurantMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantHandler implements IRestaurantHandler {

    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantRequestMapper restaurantRequestMapper;
    private final IRestaurantResponseMapper restaurantResponseMapper;

    @Override
    public RestaurantResponseDto saveRestaurant(RestaurantRequestDto restaurantRequestDto) {
        RestaurantModel restaurantModel = restaurantRequestMapper.toRestaurant(restaurantRequestDto);
        restaurantModel = restaurantServicePort.saveRestaurant(restaurantModel);
        return restaurantResponseMapper.toResponse(restaurantModel.getId(), RestaurantMessages.RESTAURANT_CREATED);
    }

    @Override
    public Page<RestaurantListResponseDto> getAllRestaurants(int page, int size) {
        Page<RestaurantModel> restaurantModels = restaurantServicePort.getAllRestaurants(page, size);
        return restaurantModels.map(restaurantResponseMapper::toResponse);
    }
}
