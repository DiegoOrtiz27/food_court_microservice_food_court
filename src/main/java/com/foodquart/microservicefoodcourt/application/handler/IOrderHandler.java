package com.foodquart.microservicefoodcourt.application.handler;

import com.foodquart.microservicefoodcourt.application.dto.request.OrderDeliveryRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.request.OrderRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.response.OrderListResponseDto;
import com.foodquart.microservicefoodcourt.application.dto.response.OrderResponseDto;
import com.foodquart.microservicefoodcourt.domain.util.OrderStatus;
import org.springframework.data.domain.Page;

public interface IOrderHandler {
    OrderResponseDto createOrder(OrderRequestDto orderRequestDto, Long customerId);

    Page<OrderListResponseDto> getOrdersByRestaurant(Long employeeId, Long restaurantId, OrderStatus status, int page, int size);

    OrderResponseDto assignOrderToEmployee(Long orderId, Long employeeId);

    OrderResponseDto notifyOrderReady(Long orderId, Long employeeId);

    OrderResponseDto markOrderAsDelivered(Long orderId, Long employeeId, OrderDeliveryRequestDto orderDeliveryRequestDto);

    OrderResponseDto cancelOrder(Long orderId, Long customerId);

}
