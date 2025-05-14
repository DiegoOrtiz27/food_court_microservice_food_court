package com.foodquart.microservicefoodcourt.domain.api;

import com.foodquart.microservicefoodcourt.domain.model.RestaurantEmployeeModel;

public interface IRestaurantEmployeeServicePort {
    RestaurantEmployeeModel addEmployeeToRestaurant(Long restaurantId, RestaurantEmployeeModel restaurantEmployeeModel);
}
