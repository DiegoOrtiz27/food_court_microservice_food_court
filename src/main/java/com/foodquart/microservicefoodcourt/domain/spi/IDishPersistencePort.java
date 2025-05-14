package com.foodquart.microservicefoodcourt.domain.spi;

import com.foodquart.microservicefoodcourt.domain.model.DishModel;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface IDishPersistencePort {

    DishModel saveDish(DishModel dishModel);

    Optional<DishModel> findById(Long id);

    Page<DishModel> findByRestaurantIdAndCategory(Long restaurantId, String category, int page, int size);

}
