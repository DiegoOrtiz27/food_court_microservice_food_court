package com.foodquart.microservicefoodcourt.domain.api;

import com.foodquart.microservicefoodcourt.domain.model.DishModel;

public interface ICreateDishServicePort {
    DishModel createDish(DishModel dishModel, Long ownerId);
}
