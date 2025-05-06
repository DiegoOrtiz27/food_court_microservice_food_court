package com.foodquart.microservicefoodcourt.infrastructure.out.jpa.repository;

import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.entity.RestaurantEmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRestaurantEmployeeRepository extends JpaRepository<RestaurantEmployeeEntity, Long> {
}
