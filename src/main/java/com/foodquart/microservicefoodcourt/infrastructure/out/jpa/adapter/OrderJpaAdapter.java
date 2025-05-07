package com.foodquart.microservicefoodcourt.infrastructure.out.jpa.adapter;

import com.foodquart.microservicefoodcourt.domain.model.OrderModel;
import com.foodquart.microservicefoodcourt.domain.spi.IOrderPersistencePort;
import com.foodquart.microservicefoodcourt.domain.util.OrderStatus;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.entity.OrderEntity;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OrderJpaAdapter implements IOrderPersistencePort {

    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;

    @Override
    public OrderModel saveOrder(OrderModel orderModel) {
        OrderEntity orderEntity = orderEntityMapper.toEntity(orderModel);
        return orderEntityMapper.toModel(orderRepository.save(orderEntity));
    }

    @Override
    public boolean hasActiveOrders(Long customerId) {
        return orderRepository.countByCustomerIdAndStatusIn(
                customerId,
                List.of(OrderStatus.PENDING, OrderStatus.IN_PREPARATION, OrderStatus.READY)
        ) > 0;
    }
}
