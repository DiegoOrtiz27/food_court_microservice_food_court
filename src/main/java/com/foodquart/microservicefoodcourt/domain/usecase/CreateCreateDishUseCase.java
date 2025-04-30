package com.foodquart.microservicefoodcourt.domain.usecase;

import com.foodquart.microservicefoodcourt.domain.api.ICreateDishServicePort;
import com.foodquart.microservicefoodcourt.domain.exception.DomainException;
import com.foodquart.microservicefoodcourt.domain.exception.InvalidOwnerException;
import com.foodquart.microservicefoodcourt.domain.exception.InvalidRestaurantException;
import com.foodquart.microservicefoodcourt.domain.model.DishModel;
import com.foodquart.microservicefoodcourt.domain.spi.IDishPersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantPersistencePort;

public class CreateCreateDishUseCase implements ICreateDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;

    public CreateCreateDishUseCase(IDishPersistencePort dishPersistencePort,
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
            throw new InvalidOwnerException(ownerId, dishModel.getRestaurantId());
        }

        dishModel.setActive(true);

        return dishPersistencePort.saveDish(dishModel);
    }

    private void validateDish(DishModel dish) {
        if (dish.getPrice() <= 0) {
            throw new DomainException("Price must be positive");
        }
    }
}
