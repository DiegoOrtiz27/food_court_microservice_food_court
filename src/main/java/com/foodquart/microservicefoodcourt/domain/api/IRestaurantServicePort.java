package com.foodquart.microservicefoodcourt.domain.api;

import com.foodquart.microservicefoodcourt.domain.model.RestaurantModel;
import com.foodquart.microservicefoodcourt.domain.util.Pagination;
public interface IRestaurantServicePort {
    RestaurantModel saveRestaurant(RestaurantModel restaurantModel);

    Pagination<RestaurantModel> getAllRestaurants(int page, int size);
}
