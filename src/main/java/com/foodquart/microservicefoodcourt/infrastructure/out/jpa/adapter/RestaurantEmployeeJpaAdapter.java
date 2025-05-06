package com.foodquart.microservicefoodcourt.infrastructure.out.jpa.adapter;

import com.foodquart.microservicefoodcourt.domain.model.RestaurantEmployeeModel;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantEmployeePersistencePort;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.entity.RestaurantEmployeeEntity;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.mapper.IRestaurantEmployeeEntityMapper;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.repository.IRestaurantEmployeeRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestaurantEmployeeJpaAdapter implements IRestaurantEmployeePersistencePort {

    private final IRestaurantEmployeeRepository restaurantEmployeeRepository;
    private final IRestaurantEmployeeEntityMapper restaurantEmployeeEntityMapper;

    @Override
    public RestaurantEmployeeModel addEmployeeToRestaurant(RestaurantEmployeeModel restaurantEmployeeModel) {
        RestaurantEmployeeEntity saved = restaurantEmployeeRepository.save(restaurantEmployeeEntityMapper.toEntity(restaurantEmployeeModel));
        return restaurantEmployeeEntityMapper.toRestaurantEmployeeModel(saved);
    }

}
