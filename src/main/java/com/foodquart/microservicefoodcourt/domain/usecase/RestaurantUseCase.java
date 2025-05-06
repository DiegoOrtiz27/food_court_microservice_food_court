package com.foodquart.microservicefoodcourt.domain.usecase;

import com.foodquart.microservicefoodcourt.domain.api.IRestaurantServicePort;
import com.foodquart.microservicefoodcourt.domain.exception.DomainException;
import com.foodquart.microservicefoodcourt.domain.exception.NitAlreadyExistsException;
import com.foodquart.microservicefoodcourt.domain.model.RestaurantModel;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantPersistencePort;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public void saveRestaurant(RestaurantModel restaurantModel) {
        validateRestaurantData(restaurantModel);

        if (restaurantPersistencePort.existsByNit(restaurantModel.getNit())) {
            throw new NitAlreadyExistsException(restaurantModel.getNit());
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
        if (!restaurant.getPhone().matches("^\\+?\\d{10,13}$")) {
            throw new DomainException("Phone should have maximum 13 characters and can include '+'");
        }
    }
}
