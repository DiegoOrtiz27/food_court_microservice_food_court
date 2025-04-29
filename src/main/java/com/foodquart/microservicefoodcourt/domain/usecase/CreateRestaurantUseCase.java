package com.foodquart.microservicefoodcourt.domain.usecase;

import com.foodquart.microservicefoodcourt.domain.api.IRestaurantServicePort;
import com.foodquart.microservicefoodcourt.domain.exception.DomainException;
import com.foodquart.microservicefoodcourt.domain.exception.InvalidOwnerRoleException;
import com.foodquart.microservicefoodcourt.domain.exception.NitAlreadyExistsException;
import com.foodquart.microservicefoodcourt.domain.model.RestaurantModel;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IUserClientPort;

public class CreateRestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserClientPort userClientPort;

    public CreateRestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort, IUserClientPort userClientPort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userClientPort = userClientPort;
    }

    @Override
    public void saveRestaurant(RestaurantModel restaurantModel) {
        validateRestaurantData(restaurantModel);

        if (restaurantPersistencePort.existsByNit(restaurantModel.getNit())) {
            throw new NitAlreadyExistsException(restaurantModel.getNit());
        }
        boolean isOwner = userClientPort.findOwnerById(restaurantModel.getOwnerId());
        if (!isOwner) {
            throw new InvalidOwnerRoleException(restaurantModel.getOwnerId());
        }

        restaurantPersistencePort.saveRestaurant(restaurantModel);
    }

    private void validateRestaurantData(RestaurantModel restaurant) {
        if (restaurant.getName().matches("\\d+")) {
            throw new DomainException("Restaurant name cannot contain only numbers.");
        }
        if (!restaurant.getNit().matches("\\d+")) {
            throw new DomainException("NIT must contain only numbers.");
        }
        if (!restaurant.getPhone().matches("^\\+?\\d{1,13}$")) {
            throw new DomainException("Phone must be numeric and up to 13 characters.");
        }
    }
}
