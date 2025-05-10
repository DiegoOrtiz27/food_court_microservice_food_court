package com.foodquart.microservicefoodcourt.domain.usecase;

import com.foodquart.microservicefoodcourt.domain.api.IDishServicePort;
import com.foodquart.microservicefoodcourt.domain.exception.DomainException;
import com.foodquart.microservicefoodcourt.domain.model.DishModel;
import com.foodquart.microservicefoodcourt.domain.spi.IDishPersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodquart.microservicefoodcourt.domain.util.DishMessages;
import com.foodquart.microservicefoodcourt.domain.util.RestaurantMessages;
import org.springframework.data.domain.Page;

import java.util.Optional;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;

    public DishUseCase(IDishPersistencePort dishPersistencePort,IRestaurantPersistencePort restaurantPersistencePort) {
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public DishModel createDish(DishModel dishModel, Long ownerId) {
        validateDish(dishModel);

        if (!restaurantPersistencePort.existsById(dishModel.getRestaurantId())) {
            throw new DomainException(String.format(RestaurantMessages.RESTAURANT_NOT_FOUND, dishModel.getRestaurantId()));
        }

        if (!restaurantPersistencePort.isOwnerOfRestaurant(ownerId, dishModel.getRestaurantId())) {
            throw new DomainException(String.format(RestaurantMessages.OWNER_NOT_ASSOCIATED_TO_RESTAURANT, dishModel.getRestaurantId()));
        }

        dishModel.setActive(true);

        return dishPersistencePort.saveDish(dishModel);
    }

    @Override
    public void updateDish(DishModel dishModel, Long ownerId) {
        validateDish(dishModel);

        Optional<DishModel> existingDish = validateExistingDish(dishModel.getId());

        existingDish.ifPresent(dish -> {
            validateUpdateDish(dish.getRestaurantId(), ownerId);
            dish.setDescription(dishModel.getDescription());
            dish.setPrice(dishModel.getPrice());
            dishPersistencePort.updateDish(dish);
        });
    }

    @Override
    public DishModel enableOrDisableDish(DishModel dishModel, Long ownerId) {
        Optional<DishModel> existingDish = validateExistingDish(dishModel.getId());

        return existingDish.map(dish -> {
            validateUpdateDish(dish.getRestaurantId(), ownerId);

            dish.setActive(dishModel.getActive());
            return dishPersistencePort.updateDishStatus(dish);
        }).orElse(null);
    }

    @Override
    public Page<DishModel> getDishesByRestaurant(Long restaurantId, String category, int page, int size) {
        if (!restaurantPersistencePort.existsById(restaurantId)) {
            throw new DomainException(String.format(RestaurantMessages.RESTAURANT_NOT_FOUND, restaurantId));
        }
        return dishPersistencePort.findByRestaurantIdAndCategory(restaurantId, category, page, size);
    }

    private void validateDish(DishModel dish) {
        if (dish.getPrice() <= 0) {
            throw new DomainException(DishMessages.PRICE_MUST_BE_POSITIVE);
        }
    }

    private Optional<DishModel> validateExistingDish(Long dishId) {
        Optional<DishModel> existingDish = dishPersistencePort.findById(dishId);
        if(existingDish.isEmpty()) {
            throw new DomainException(String.format(DishMessages.DISH_NOT_FOUND, dishId));
        }
        return existingDish;
    }

    private void validateUpdateDish(Long restaurantId, Long ownerId) {

        if (!restaurantPersistencePort.existsById(restaurantId)) {
            throw new DomainException(String.format(RestaurantMessages.RESTAURANT_NOT_FOUND, restaurantId));
        }

        if (!restaurantPersistencePort.isOwnerOfRestaurant(ownerId, restaurantId)) {
            throw new DomainException(String.format(RestaurantMessages.OWNER_NOT_ASSOCIATED_TO_RESTAURANT, restaurantId));
        }
    }
}
