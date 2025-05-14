package com.foodquart.microservicefoodcourt.application.handler;

import com.foodquart.microservicefoodcourt.application.dto.request.OrderDeliveryRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.request.OrderRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.response.OrderListResponseDto;
import com.foodquart.microservicefoodcourt.application.dto.response.OrderResponseDto;
import com.foodquart.microservicefoodcourt.domain.util.OrderStatus;
import org.springframework.data.domain.Page;

public interface IOrderHandler {
    OrderResponseDto createOrder(OrderRequestDto orderRequestDto);

    Page<OrderListResponseDto> getOrdersByRestaurant(Long restaurantId, OrderStatus status, int page, int size);

    OrderResponseDto assignOrderToEmployee(Long orderId);

    OrderResponseDto notifyOrderReady(Long orderId);

    OrderResponseDto markOrderAsDelivered(Long orderId, OrderDeliveryRequestDto orderDeliveryRequestDto);

    OrderResponseDto cancelOrder(Long orderId);

}
