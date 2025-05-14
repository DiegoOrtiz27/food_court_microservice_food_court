package com.foodquart.microservicefoodcourt.domain.usecase;

import com.foodquart.microservicefoodcourt.domain.api.IRestaurantServicePort;
import com.foodquart.microservicefoodcourt.domain.exception.DomainException;
import com.foodquart.microservicefoodcourt.domain.model.RestaurantModel;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodquart.microservicefoodcourt.domain.util.Pagination;

import static com.foodquart.microservicefoodcourt.domain.util.RestaurantMessages.NIT_ALREADY_EXISTS;
import static com.foodquart.microservicefoodcourt.domain.util.ValidationUtil.validatePagination;
import static com.foodquart.microservicefoodcourt.domain.util.ValidationUtil.validateRestaurantData;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public RestaurantModel saveRestaurant(RestaurantModel restaurantModel) {
        validateRestaurantData(restaurantModel);

        if (restaurantPersistencePort.existsByNit(restaurantModel.getNit())) {
            throw new DomainException(String.format(NIT_ALREADY_EXISTS, restaurantModel.getNit()));
        }

        return restaurantPersistencePort.saveRestaurant(restaurantModel);
    }

    @Override
    public Pagination<RestaurantModel> getAllRestaurants(int page, int size) {
        validatePagination(page, size);
        return restaurantPersistencePort.getAllRestaurants(page, size);
    }
}
