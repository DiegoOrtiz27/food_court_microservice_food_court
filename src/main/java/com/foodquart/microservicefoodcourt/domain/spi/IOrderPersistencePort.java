package com.foodquart.microservicefoodcourt.domain.spi;

import com.foodquart.microservicefoodcourt.domain.model.OrderModel;
import com.foodquart.microservicefoodcourt.domain.util.OrderStatus;
import org.springframework.data.domain.Page;

public interface IOrderPersistencePort {

    OrderModel saveOrder(OrderModel orderModel);

    boolean hasActiveOrders(Long customerId);

    Page<OrderModel> findByRestaurantIdAndStatus(Long restaurantId, OrderStatus status, int page, int size);
}
