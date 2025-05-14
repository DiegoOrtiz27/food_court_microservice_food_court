package com.foodquart.microservicefoodcourt.domain.api;

import com.foodquart.microservicefoodcourt.domain.model.DishModel;
import org.springframework.data.domain.Page;

public interface IDishServicePort {
    DishModel createDish(DishModel dishModel);

    void updateDish(Long dishId, DishModel dishModel);

    DishModel enableOrDisableDish(Long dishId, DishModel dishModel);

    Page<DishModel> getDishesByRestaurant(Long restaurantId, String category, int page, int size);
}
