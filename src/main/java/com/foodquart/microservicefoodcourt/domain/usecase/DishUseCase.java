package com.foodquart.microservicefoodcourt.domain.usecase;

import com.foodquart.microservicefoodcourt.domain.api.IDishServicePort;
import com.foodquart.microservicefoodcourt.domain.model.DishModel;
import com.foodquart.microservicefoodcourt.domain.spi.IDishPersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.ISecurityContextPort;
import org.springframework.data.domain.Page;

import static com.foodquart.microservicefoodcourt.domain.util.ValidationUtil.*;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final ISecurityContextPort securityContextPort;

    public DishUseCase(IDishPersistencePort dishPersistencePort, IRestaurantPersistencePort restaurantPersistencePort, ISecurityContextPort securityContextPort) {
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.securityContextPort = securityContextPort;
    }

    @Override
    public DishModel createDish(DishModel dishModel) {
        Long ownerId = securityContextPort.getCurrentUserId();

        validateDish(dishModel);
        validateRestaurant(dishModel.getRestaurantId(), ownerId);

        dishModel.setActive(true);

        return dishPersistencePort.saveDish(dishModel);
    }

    @Override
    public void updateDish(Long dishId, DishModel dishModel) {
        Long ownerId = securityContextPort.getCurrentUserId();

        validateDish(dishModel);
        DishModel existingDish = validateExistingDish(dishPersistencePort, dishId);

        validateRestaurant(existingDish.getRestaurantId(), ownerId);
        existingDish.setDescription(dishModel.getDescription());
        existingDish.setPrice(dishModel.getPrice());
        dishPersistencePort.saveDish(existingDish);
    }

    @Override
    public DishModel enableOrDisableDish(Long dishId, DishModel dishModel) {
        Long ownerId = securityContextPort.getCurrentUserId();

        DishModel existingDish = validateExistingDish(dishPersistencePort, dishId);
        validateRestaurant(existingDish.getRestaurantId(), ownerId);

        existingDish.setActive(dishModel.getActive());
        return dishPersistencePort.saveDish(existingDish);
    }

    @Override
    public Page<DishModel> getDishesByRestaurant(Long restaurantId, String category, int page, int size) {
        existsRestaurantById(restaurantPersistencePort, restaurantId);
        return dishPersistencePort.findByRestaurantIdAndCategory(restaurantId, category, page, size);
    }


    private void validateRestaurant(Long restaurantId, Long ownerId) {
        existsRestaurantById(restaurantPersistencePort, restaurantId);
        isOwnerOfRestaurant(restaurantPersistencePort, restaurantId, ownerId);
    }
}
