package com.foodquart.microservicefoodcourt.infrastructure.out.jpa.adapter;

import com.foodquart.microservicefoodcourt.domain.model.DishModel;
import com.foodquart.microservicefoodcourt.domain.spi.IDishPersistencePort;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.entity.DishEntity;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.mapper.IDishEntityMapper;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.repository.IDishRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DishJpaAdapter implements IDishPersistencePort {

    private final IDishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;

    @Override
    public DishModel saveDish(DishModel dishModel) {
        DishEntity entity = dishEntityMapper.toEntity(dishModel);
        DishEntity savedEntity = dishRepository.save(entity);
        return dishEntityMapper.toDishModel(savedEntity);
    }

    @Override
    public boolean existsByRestaurantId(Long restaurantId) {
        return dishRepository.existsByRestaurantId(restaurantId);
    }
}
