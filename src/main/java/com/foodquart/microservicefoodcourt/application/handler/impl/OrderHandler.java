package com.foodquart.microservicefoodcourt.application.handler.impl;

import com.foodquart.microservicefoodcourt.application.dto.request.OrderDeliveryRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.response.OrderListResponseDto;
import com.foodquart.microservicefoodcourt.application.dto.request.OrderRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.response.OrderResponseDto;
import com.foodquart.microservicefoodcourt.application.handler.IOrderHandler;
import com.foodquart.microservicefoodcourt.application.mapper.request.IOrderRequestMapper;
import com.foodquart.microservicefoodcourt.application.mapper.response.IOrderResponseMapper;
import com.foodquart.microservicefoodcourt.domain.api.IOrderServicePort;
import com.foodquart.microservicefoodcourt.domain.model.OrderModel;
import com.foodquart.microservicefoodcourt.domain.util.OrderStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import static com.foodquart.microservicefoodcourt.domain.util.OrderMessages.*;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderHandler implements IOrderHandler {
    private final IOrderServicePort orderServicePort;
    private final IOrderRequestMapper orderRequestMapper;
    private final IOrderResponseMapper orderResponseMapper;

    @Override
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        OrderModel orderModel = orderRequestMapper.toOrder(orderRequestDto);
        orderModel = orderServicePort.createOrder(orderModel);
        return orderResponseMapper.toResponse(orderModel.getId(), orderModel.getStatus().toString(), CREATED_ORDER);
    }

    @Override
    public Page<OrderListResponseDto> getOrdersByRestaurant(Long restaurantId, OrderStatus status, int page, int size) {
        Page<OrderModel> orderModel = orderServicePort.getOrdersByRestaurant(restaurantId, status, page, size);
        return orderModel.map(orderResponseMapper::toResponse);
    }

    @Override
    public OrderResponseDto assignOrderToEmployee(Long orderId) {
        OrderModel orderModel = orderServicePort.assignOrderToEmployee(orderId);
        return orderResponseMapper.toResponse(orderModel.getId(), orderModel.getStatus().toString(), ASSIGNED_ORDER);
    }

    @Override
    public OrderResponseDto notifyOrderReady(Long orderId) {
        OrderModel orderModel = orderServicePort.notifyOrderReady(orderId);
        return orderResponseMapper.toResponse(orderModel.getId(), orderModel.getStatus().toString(), ORDER_READY);
    }

    @Override
    public OrderResponseDto markOrderAsDelivered(Long orderId, OrderDeliveryRequestDto orderDeliveryRequestDto) {
        OrderModel orderModel = orderServicePort.markOrderAsDelivered(orderId, orderDeliveryRequestDto.getSecurityPin());
        return orderResponseMapper.toResponse(orderModel.getId(), orderModel.getStatus().toString(), ORDER_DELIVERED);
    }

    @Override
    public OrderResponseDto cancelOrder(Long orderId) {
        OrderModel orderModel = orderServicePort.cancelOrder(orderId);
        return orderResponseMapper.toResponse(orderModel.getId(), orderModel.getStatus().toString(), ORDER_CANCELLED);
    }
}
