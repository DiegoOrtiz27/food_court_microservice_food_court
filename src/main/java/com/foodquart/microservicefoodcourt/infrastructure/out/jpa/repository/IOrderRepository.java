package com.foodquart.microservicefoodcourt.infrastructure.out.jpa.repository;

import com.foodquart.microservicefoodcourt.domain.util.OrderStatus;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.customerId = :customerId AND o.status IN :statuses")
    long countByCustomerIdAndStatusIn(Long customerId, List<OrderStatus> statuses);

    Page<OrderEntity> findByRestaurantId(Long restaurantId, Pageable pageable);

    @EntityGraph(attributePaths = {"items", "items.dish"})
    Page<OrderEntity> findByRestaurantIdAndStatus(Long restaurantId, OrderStatus status, Pageable pageable);

}
