package com.foodquart.microservicefoodcourt.domain.usecase;

import com.foodquart.microservicefoodcourt.domain.api.IRestaurantServicePort;
import com.foodquart.microservicefoodcourt.domain.exception.DomainException;
import com.foodquart.microservicefoodcourt.domain.model.RestaurantModel;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodquart.microservicefoodcourt.domain.util.RestaurantMessages;
import com.foodquart.microservicefoodcourt.domain.util.ValidationUtil;
import org.springframework.data.domain.Page;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public RestaurantModel saveRestaurant(RestaurantModel restaurantModel) {
        ValidationUtil.validateRestaurantData(restaurantModel);

        if (restaurantPersistencePort.existsByNit(restaurantModel.getNit())) {
            throw new DomainException(String.format(RestaurantMessages.NIT_ALREADY_EXISTS, restaurantModel.getNit()));
        }

        return restaurantPersistencePort.saveRestaurant(restaurantModel);
    }

    @Override
    public Page<RestaurantModel> getAllRestaurants(int page, int size) {
        return restaurantPersistencePort.getAllRestaurants(page, size);
    }
}
