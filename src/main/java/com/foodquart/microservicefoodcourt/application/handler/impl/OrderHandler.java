package com.foodquart.microservicefoodcourt.application.handler.impl;

import com.foodquart.microservicefoodcourt.application.dto.response.OrderListResponseDto;
import com.foodquart.microservicefoodcourt.application.dto.request.OrderRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.response.OrderResponseDto;
import com.foodquart.microservicefoodcourt.application.handler.IOrderHandler;
import com.foodquart.microservicefoodcourt.application.mapper.IOrderRequestMapper;
import com.foodquart.microservicefoodcourt.application.mapper.IOrderResponseMapper;
import com.foodquart.microservicefoodcourt.domain.api.IOrderServicePort;
import com.foodquart.microservicefoodcourt.domain.model.OrderModel;
import com.foodquart.microservicefoodcourt.domain.util.OrderMessages;
import com.foodquart.microservicefoodcourt.domain.util.OrderStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderHandler implements IOrderHandler {
    private final IOrderServicePort orderServicePort;
    private final IOrderRequestMapper orderRequestMapper;
    private final IOrderResponseMapper orderResponseMapper;

    @Override
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto, Long customerId) {
        OrderModel orderModel = orderRequestMapper.toOrder(orderRequestDto);
        orderModel.setCustomerId(customerId);
        orderModel = orderServicePort.createOrder(orderModel);
        return orderResponseMapper.toResponse(orderModel.getId(), orderModel.getStatus().toString(), OrderMessages.CREATED_ORDER);
    }

    @Override
    public Page<OrderListResponseDto> getOrdersByRestaurant(Long employeeId, Long restaurantId, OrderStatus status, int page, int size) {
        Page<OrderModel> orderModel = orderServicePort.getOrdersByRestaurant(employeeId, restaurantId, status, page, size);
        return orderModel.map(orderResponseMapper::toResponse);
    }

    @Override
    public OrderResponseDto assignOrderToEmployee(Long orderId, Long employeeId) {
        OrderModel orderModel = orderServicePort.assignOrderToEmployee(orderId, employeeId);
        return orderResponseMapper.toResponse(orderModel.getId(), orderModel.getStatus().toString(), OrderMessages.ASSIGNED_ORDER);
    }

    @Override
    public OrderResponseDto notifyOrderReady(Long orderId, Long employeeId) {
        OrderModel orderModel = orderServicePort.notifyOrderReady(orderId, employeeId);
        return orderResponseMapper.toResponse(orderModel.getId(), orderModel.getStatus().toString(), OrderMessages.ORDER_READY);
    }
}
