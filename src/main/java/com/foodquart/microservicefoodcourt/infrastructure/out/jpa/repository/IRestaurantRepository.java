package com.foodquart.microservicefoodcourt.infrastructure.out.jpa.repository;

import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    boolean existsByNit(String nit);

    boolean existsByIdAndOwnerId(Long id, Long ownerId);

    boolean existsById(Long id);
}
