package com.foodquart.microservicefoodcourt.domain.spi;

import com.foodquart.microservicefoodcourt.domain.model.DishModel;

public interface IDishPersistencePort {
    DishModel saveDish(DishModel dishModel);
    boolean existsByRestaurantId(Long restaurantId);
}
