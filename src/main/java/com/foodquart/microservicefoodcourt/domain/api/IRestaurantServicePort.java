package com.foodquart.microservicefoodcourt.domain.api;

import com.foodquart.microservicefoodcourt.domain.model.RestaurantModel;
import org.springframework.data.domain.Page;

public interface IRestaurantServicePort {
    RestaurantModel saveRestaurant(RestaurantModel restaurantModel);

    Page<RestaurantModel> getAllRestaurants(int page, int size);
}
