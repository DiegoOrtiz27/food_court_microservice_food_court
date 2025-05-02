package com.foodquart.microservicefoodcourt.domain.api;

import com.foodquart.microservicefoodcourt.domain.model.DishModel;

public interface IDishServicePort {
    DishModel createDish(DishModel dishModel);

    void updateDish(DishModel dishModel);
}
