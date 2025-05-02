package com.foodquart.microservicefoodcourt.application.handler;

import com.foodquart.microservicefoodcourt.application.dto.RestaurantRequestDto;

public interface IRestaurantHandler {
    void saveRestaurant(RestaurantRequestDto restaurantRequestDto);
}