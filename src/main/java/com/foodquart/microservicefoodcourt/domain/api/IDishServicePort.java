package com.foodquart.microservicefoodcourt.domain.api;

import com.foodquart.microservicefoodcourt.domain.model.DishModel;
import com.foodquart.microservicefoodcourt.domain.util.Pagination;

public interface IDishServicePort {
    DishModel createDish(DishModel dishModel);

    void updateDish(Long dishId, DishModel dishModel);

    DishModel enableOrDisableDish(Long dishId, DishModel dishModel);

    Pagination<DishModel> getDishesByRestaurant(Long restaurantId, String category, int page, int size);
}
