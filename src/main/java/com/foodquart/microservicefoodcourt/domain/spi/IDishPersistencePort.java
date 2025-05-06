package com.foodquart.microservicefoodcourt.domain.spi;

import com.foodquart.microservicefoodcourt.domain.model.DishModel;

import java.util.Optional;

public interface IDishPersistencePort {

    DishModel saveDish(DishModel dishModel);

    Optional<DishModel> findById(Long id);

    DishModel updateDish(DishModel dishModel);



}
