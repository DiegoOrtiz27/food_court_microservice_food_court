package com.foodquart.microservicefoodcourt.domain.spi;

import com.foodquart.microservicefoodcourt.domain.model.OrderModel;
import com.foodquart.microservicefoodcourt.domain.util.OrderStatus;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IOrderPersistencePort {

    OrderModel saveOrder(OrderModel orderModel);

    boolean hasActiveOrders(Long customerId, List<OrderStatus> statuses);

    Page<OrderModel> findByRestaurantIdAndStatus(Long restaurantId, OrderStatus status, int page, int size);

    Optional<OrderModel> findById(Long id);

    OrderModel updateOrder(OrderModel orderModel);

    boolean hasAssignedOrder(Long employeeId, Long orderId);

}
