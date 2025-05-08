package com.foodquart.microservicefoodcourt.domain.api;

import com.foodquart.microservicefoodcourt.domain.model.OrderModel;
import com.foodquart.microservicefoodcourt.domain.util.OrderStatus;
import org.springframework.data.domain.Page;

public interface IOrderServicePort {

    OrderModel createOrder(OrderModel orderModel);

    Page<OrderModel> getOrdersByRestaurant(Long employeeId, Long restaurantId, OrderStatus status, int page, int size);

    OrderModel assignOrderToEmployee(Long orderId, Long employeeId);
}
