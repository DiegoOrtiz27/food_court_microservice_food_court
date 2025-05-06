package com.foodquart.microservicefoodcourt.infrastructure.out.jpa.adapter;

import com.foodquart.microservicefoodcourt.domain.model.DishModel;
import com.foodquart.microservicefoodcourt.domain.spi.IDishPersistencePort;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.entity.DishEntity;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.mapper.IDishEntityMapper;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.repository.IDishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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
        Optional<DishEntity> existingDishEntity = dishRepository.findById(dish.getId());
        if (existingDishEntity.isPresent()) {
            DishEntity dishEntity = dishEntityMapper.toEntity(dish);
            DishEntity updatedEntity = dishRepository.save(dishEntity);
            return dishEntityMapper.toDishModel(updatedEntity);
        }
        return null;
    }

    @Override
    public DishModel updateDishStatus(DishModel dishModel) {
        Optional<DishEntity> existingDishEntity = dishRepository.findById(dishModel.getId());
        if (existingDishEntity.isPresent()) {
            DishEntity dishEntity = existingDishEntity.get();
            dishEntity.setActive(dishModel.getActive());
            DishEntity updatedEntity = dishRepository.save(dishEntity);
            return dishEntityMapper.toDishModel(updatedEntity);
        }
        return null;
    }

    @Override
    public Page<DishModel> findByRestaurantIdAndCategory(Long restaurantId, String category, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return (category == null || category.isEmpty())
                ? dishRepository.findByRestaurantId(restaurantId, pageRequest).map(dishEntityMapper::toDishModel)
                : dishRepository.findByRestaurantIdAndCategory(restaurantId, category, pageRequest).map(dishEntityMapper::toDishModel);
    }
}
