package com.foodquart.microservicefoodcourt.application.handler;

import com.foodquart.microservicefoodcourt.application.dto.response.PaginationListResponseDto;
import com.foodquart.microservicefoodcourt.application.dto.request.RestaurantRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.response.RestaurantItemResponse;
import com.foodquart.microservicefoodcourt.application.dto.response.RestaurantResponseDto;

public interface IRestaurantHandler {
    RestaurantResponseDto saveRestaurant(RestaurantRequestDto restaurantRequestDto);

    PaginationListResponseDto<RestaurantItemResponse> getAllRestaurants(int page, int size);
}