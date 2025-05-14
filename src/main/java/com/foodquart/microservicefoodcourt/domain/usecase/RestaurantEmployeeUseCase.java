package com.foodquart.microservicefoodcourt.domain.usecase;

import com.foodquart.microservicefoodcourt.domain.api.IRestaurantEmployeeServicePort;
import com.foodquart.microservicefoodcourt.domain.model.RestaurantEmployeeModel;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantEmployeePersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.ISecurityContextPort;
import com.foodquart.microservicefoodcourt.domain.spi.IUserClientPort;

import static com.foodquart.microservicefoodcourt.domain.util.ValidationUtil.existsRestaurantById;
import static com.foodquart.microservicefoodcourt.domain.util.ValidationUtil.isOwnerOfRestaurant;

public class RestaurantEmployeeUseCase implements IRestaurantEmployeeServicePort {

    private final IRestaurantEmployeePersistencePort employeePersistence;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserClientPort userClientPort;
    private final ISecurityContextPort securityContextPort;

    public RestaurantEmployeeUseCase(IRestaurantEmployeePersistencePort employeePersistence, IRestaurantPersistencePort restaurantPersistencePort, IUserClientPort userClientPort, ISecurityContextPort securityContextPort) {
        this.employeePersistence = employeePersistence;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userClientPort = userClientPort;
        this.securityContextPort = securityContextPort;
    }

    @Override
    public RestaurantEmployeeModel addEmployeeToRestaurant(Long restaurantId, RestaurantEmployeeModel restaurantEmployeeModel) {
        Long ownerId = securityContextPort.getCurrentUserId();

        existsRestaurantById(restaurantPersistencePort, restaurantId);
        isOwnerOfRestaurant(restaurantPersistencePort, restaurantId, ownerId);

        restaurantEmployeeModel.setRestaurantId(restaurantId);
        return employeePersistence.addEmployeeToRestaurant(userClientPort.createEmployee(restaurantEmployeeModel));
    }
}
