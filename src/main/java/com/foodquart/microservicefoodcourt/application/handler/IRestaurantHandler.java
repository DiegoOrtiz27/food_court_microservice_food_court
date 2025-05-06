package com.foodquart.microservicefoodcourt.application.handler;

import com.foodquart.microservicefoodcourt.application.dto.GetRestaurantResponseDto;
import com.foodquart.microservicefoodcourt.application.dto.RestaurantRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.RestaurantResponseDto;
import org.springframework.data.domain.Page;

public interface IRestaurantHandler {
    RestaurantResponseDto saveRestaurant(RestaurantRequestDto restaurantRequestDto);

    Page<GetRestaurantResponseDto> getAllRestaurants(int page, int size);
}