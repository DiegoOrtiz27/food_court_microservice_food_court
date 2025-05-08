package com.foodquart.microservicefoodcourt.application.handler;

import com.foodquart.microservicefoodcourt.application.dto.*;
import com.foodquart.microservicefoodcourt.domain.util.OrderStatus;
import org.springframework.data.domain.Page;

public interface IOrderHandler {
    OrderResponseDto createOrder(OrderRequestDto orderRequestDto, Long customerId);

    Page<OrderListResponseDto> getOrdersByRestaurant(Long employeeId, Long restaurantId, OrderStatus status, int page, int size);

    OrderResponseDto assignOrderToEmployee(Long orderId, Long employeeId);
}
