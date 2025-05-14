package com.foodquart.microservicefoodcourt.domain.spi;

import com.foodquart.microservicefoodcourt.domain.model.OrderModel;
import com.foodquart.microservicefoodcourt.domain.util.OrderStatus;
import com.foodquart.microservicefoodcourt.domain.util.Pagination;

import java.util.List;
import java.util.Optional;

public interface IOrderPersistencePort {

    OrderModel saveOrder(OrderModel orderModel);

    boolean hasActiveOrders(Long customerId, List<OrderStatus> statuses);

    Pagination<OrderModel> findByRestaurantIdAndStatus(Long restaurantId, OrderStatus status, int page, int size);

    Optional<OrderModel> findById(Long id);

    boolean hasAssignedOrder(Long employeeId, Long orderId);

}
