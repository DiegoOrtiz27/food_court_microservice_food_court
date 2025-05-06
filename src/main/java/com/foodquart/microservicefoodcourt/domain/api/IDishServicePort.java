package com.foodquart.microservicefoodcourt.domain.api;

import com.foodquart.microservicefoodcourt.domain.model.DishModel;
import org.springframework.data.domain.Page;

public interface IDishServicePort {
    DishModel createDish(DishModel dishModel, Long ownerId);

    void updateDish(DishModel dishModel, Long ownerId);

    DishModel enableOrDisableDish(DishModel dishModel, Long ownerId);

    Page<DishModel> getDishesByRestaurant(Long restaurantId, String category, int page, int size);
}
