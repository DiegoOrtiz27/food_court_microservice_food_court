package com.foodquart.microservicefoodcourt.infrastructure.out.jpa.adapter;

import com.foodquart.microservicefoodcourt.domain.model.RestaurantModel;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.entity.RestaurantEntity;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestaurantJpaAdapter implements IRestaurantPersistencePort {

    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;

    @Override
    public RestaurantModel saveRestaurant(RestaurantModel restaurant) {
        RestaurantEntity saved = restaurantRepository.save(restaurantEntityMapper.toEntity(restaurant));
        return restaurantEntityMapper.toRestaurantModel(saved);
    }

    @Override
    public boolean existsByNit(String nit) {
        return restaurantRepository.existsByNit(nit);
    }

    @Override
    public boolean isOwnerOfRestaurant(Long ownerId, Long restaurantId) {
        return restaurantRepository.existsByIdAndOwnerId(restaurantId, ownerId);
    }

    @Override
    public boolean existsById(Long id) {
        return restaurantRepository.existsById(id);
    }
}
