package com.foodquart.microservicefoodcourt.application.handler;

import com.foodquart.microservicefoodcourt.application.dto.OrderRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.OrderResponseDto;

public interface IOrderHandler {
    OrderResponseDto createOrder(OrderRequestDto orderRequestDto, Long customerId);
}
