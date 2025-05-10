package com.foodquart.microservicefoodcourt.application.handler;

import com.foodquart.microservicefoodcourt.application.dto.response.RestaurantListResponseDto;
import com.foodquart.microservicefoodcourt.application.dto.request.RestaurantRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.response.RestaurantResponseDto;
import org.springframework.data.domain.Page;

public interface IRestaurantHandler {
    RestaurantResponseDto saveRestaurant(RestaurantRequestDto restaurantRequestDto);

    Page<RestaurantListResponseDto> getAllRestaurants(int page, int size);
}