package com.foodquart.microservicefoodcourt.infrastructure.out.jpa.adapter;

import com.foodquart.microservicefoodcourt.domain.model.DishModel;
import com.foodquart.microservicefoodcourt.domain.spi.IDishPersistencePort;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.entity.DishEntity;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.mapper.IDishEntityMapper;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.repository.IDishRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class DishJpaAdapter implements IDishPersistencePort {

    private final IDishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;

    @Override
    public DishModel saveDish(DishModel dish) {
        DishEntity dishEntity = dishEntityMapper.toEntity(dish);
        DishEntity savedEntity = dishRepository.save(dishEntity);
        return dishEntityMapper.toDishModel(savedEntity);
    }

    @Override
    public Optional<DishModel> findById(Long id) {
        return dishRepository.findById(id)
                .map(dishEntityMapper::toDishModel);
    }

    @Override
    public DishModel updateDish(DishModel dish) {
        DishEntity dishEntity = dishEntityMapper.toEntity(dish);
        DishEntity savedEntity = dishRepository.save(dishEntity);
        return dishEntityMapper.toDishModel(savedEntity);
    }
}
