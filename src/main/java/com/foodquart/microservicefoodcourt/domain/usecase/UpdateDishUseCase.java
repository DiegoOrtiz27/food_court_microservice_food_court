package com.foodquart.microservicefoodcourt.domain.usecase;

import com.foodquart.microservicefoodcourt.domain.api.IUpdateDishServicePort;
import com.foodquart.microservicefoodcourt.domain.exception.DomainException;
import com.foodquart.microservicefoodcourt.domain.exception.InvalidDishException;
import com.foodquart.microservicefoodcourt.domain.exception.InvalidOwnerException;
import com.foodquart.microservicefoodcourt.domain.exception.InvalidRestaurantException;
import com.foodquart.microservicefoodcourt.domain.model.DishModel;
import com.foodquart.microservicefoodcourt.domain.spi.IDishPersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantPersistencePort;

import java.util.Optional;

public class UpdateDishUseCase implements IUpdateDishServicePort {
    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;

    public UpdateDishUseCase(IDishPersistencePort dishPersistencePort,
                             IRestaurantPersistencePort restaurantPersistencePort) {
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public void updateDish(Long dishId, String description, Integer price, Long ownerId) {
        if (price == null || price <= 0) {
            throw new DomainException("Price must be positive");
        }

        Optional<DishModel> existingDish = dishPersistencePort.findById(dishId);
        if(existingDish.isEmpty()) {
            throw new InvalidDishException(dishId);
        }

        Long restaurantId = existingDish.get().getRestaurantId();

        if (!restaurantPersistencePort.existsById(restaurantId)) {
            throw new InvalidRestaurantException(restaurantId);
        }

        if (!restaurantPersistencePort.isOwnerOfRestaurant(ownerId, restaurantId)) {
            throw new InvalidOwnerException(ownerId, restaurantId);
        }

        existingDish.get().setDescription(description);
        existingDish.get().setPrice(price);

        dishPersistencePort.updateDish(existingDish.orElse(null));
    }
}
