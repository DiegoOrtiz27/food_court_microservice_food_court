package com.foodquart.microservicefoodcourt.domain.spi;

import com.foodquart.microservicefoodcourt.domain.model.DishModel;
import com.foodquart.microservicefoodcourt.domain.util.Pagination;

import java.util.Optional;

public interface IDishPersistencePort {

    DishModel saveDish(DishModel dishModel);

    Optional<DishModel> findById(Long id);

    Pagination<DishModel> findByRestaurantIdAndCategory(Long restaurantId, String category, int page, int size);

}
