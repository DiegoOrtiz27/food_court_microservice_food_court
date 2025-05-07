package com.foodquart.microservicefoodcourt.domain.spi;

import com.foodquart.microservicefoodcourt.domain.model.RestaurantEmployeeModel;

public interface IUserClientPort {

    RestaurantEmployeeModel createEmployee(RestaurantEmployeeModel restaurantEmployeeModel);
}
