package com.foodquart.microservicefoodcourt.domain.usecase;

import com.foodquart.microservicefoodcourt.domain.api.IRestaurantEmployeeServicePort;
import com.foodquart.microservicefoodcourt.domain.exception.DomainException;
import com.foodquart.microservicefoodcourt.domain.model.RestaurantEmployeeModel;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantEmployeePersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IUserClientPort;
import com.foodquart.microservicefoodcourt.domain.util.RestaurantMessages;

public class RestaurantEmployeeUseCase implements IRestaurantEmployeeServicePort {

    private final IRestaurantEmployeePersistencePort employeePersistence;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserClientPort userClientPort;

    public RestaurantEmployeeUseCase(IRestaurantEmployeePersistencePort employeePersistence, IRestaurantPersistencePort restaurantPersistencePort, IUserClientPort userClientPort) {
        this.employeePersistence = employeePersistence;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userClientPort = userClientPort;
    }

    @Override
    public RestaurantEmployeeModel addEmployeeToRestaurant(Long ownerId, RestaurantEmployeeModel restaurantEmployeeModel) {
        if (!restaurantPersistencePort.isOwnerOfRestaurant(ownerId, restaurantEmployeeModel.getRestaurantId())) {
            throw new DomainException(String.format(RestaurantMessages.OWNER_NOT_ASSOCIATED_TO_RESTAURANT, restaurantEmployeeModel.getRestaurantId()));
        }
        return employeePersistence.addEmployeeToRestaurant(userClientPort.createEmployee(restaurantEmployeeModel));
    }
}
