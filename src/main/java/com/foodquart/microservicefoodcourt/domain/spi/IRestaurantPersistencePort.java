package com.foodquart.microservicefoodcourt.domain.spi;

import com.foodquart.microservicefoodcourt.domain.model.RestaurantModel;
import com.foodquart.microservicefoodcourt.domain.util.Pagination;

public interface IRestaurantPersistencePort {

    RestaurantModel saveRestaurant(RestaurantModel restaurantModel);

    boolean existsByNit(String nit);

    boolean isOwnerOfRestaurant(Long ownerId, Long restaurantId);

    boolean existsById(Long id);

    Pagination<RestaurantModel> getAllRestaurants(int page, int size);

}
