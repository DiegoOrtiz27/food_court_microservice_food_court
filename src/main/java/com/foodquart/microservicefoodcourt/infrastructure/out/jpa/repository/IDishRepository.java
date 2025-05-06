package com.foodquart.microservicefoodcourt.infrastructure.out.jpa.repository;

import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.entity.DishEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

public interface IDishRepository extends JpaRepository<DishEntity, Long> {
    Page<DishEntity> findByRestaurantId(Long restaurantId, Pageable pageable);

    Page<DishEntity> findByRestaurantIdAndCategory(Long restaurantId, String category, Pageable pageable);
}
