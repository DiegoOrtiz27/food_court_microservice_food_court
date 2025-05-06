package com.foodquart.microservicefoodcourt.domain.usecase;

import com.foodquart.microservicefoodcourt.domain.api.IDishServicePort;
import com.foodquart.microservicefoodcourt.domain.exception.DomainException;
import com.foodquart.microservicefoodcourt.domain.exception.InvalidDishException;
import com.foodquart.microservicefoodcourt.domain.exception.InvalidOwnerException;
import com.foodquart.microservicefoodcourt.domain.exception.InvalidRestaurantException;
import com.foodquart.microservicefoodcourt.domain.model.DishModel;
import com.foodquart.microservicefoodcourt.domain.spi.IDishPersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantPersistencePort;

import java.util.Optional;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;

    public DishUseCase(IDishPersistencePort dishPersistencePort,
                       IRestaurantPersistencePort restaurantPersistencePort) {
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public DishModel createDish(DishModel dishModel, Long ownerId) {
        validateDish(dishModel);

        if (!restaurantPersistencePort.existsById(dishModel.getRestaurantId())) {
            throw new InvalidRestaurantException(dishModel.getRestaurantId());
        }

        if (!restaurantPersistencePort.isOwnerOfRestaurant(ownerId, dishModel.getRestaurantId())) {
            throw new InvalidOwnerException(dishModel.getRestaurantId());
        }

        dishModel.setActive(true);

        return dishPersistencePort.saveDish(dishModel);
    }

    @Override
    public void updateDish(DishModel dishModel, Long ownerId) {
        validateDish(dishModel);

        Optional<DishModel> existingDish = dishPersistencePort.findById(dishModel.getId());
        if(existingDish.isEmpty()) {
            throw new InvalidDishException(dishModel.getId());
        }

        Long restaurantId = existingDish.get().getRestaurantId();

        if (!restaurantPersistencePort.existsById(restaurantId)) {
            throw new InvalidRestaurantException(restaurantId);
        }

        if (!restaurantPersistencePort.isOwnerOfRestaurant(ownerId, restaurantId)) {
            throw new InvalidOwnerException(restaurantId);
        }

        existingDish.get().setDescription(dishModel.getDescription());
        existingDish.get().setPrice(dishModel.getPrice());

        dishPersistencePort.updateDish(existingDish.orElse(null));
    }

    private void validateDish(DishModel dish) {
        if (dish.getPrice() <= 0) {
            throw new DomainException("Price must be positive");
        }
    }
}
