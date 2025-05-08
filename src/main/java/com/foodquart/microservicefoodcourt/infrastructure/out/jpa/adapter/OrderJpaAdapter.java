package com.foodquart.microservicefoodcourt.infrastructure.out.jpa.adapter;

import com.foodquart.microservicefoodcourt.domain.model.OrderModel;
import com.foodquart.microservicefoodcourt.domain.spi.IOrderPersistencePort;
import com.foodquart.microservicefoodcourt.domain.util.OrderStatus;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.entity.OrderEntity;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@RequiredArgsConstructor
public class OrderJpaAdapter implements IOrderPersistencePort {

    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;

    @Override
    public OrderModel saveOrder(OrderModel orderModel) {
        OrderEntity orderEntity = orderEntityMapper.toEntity(orderModel);
        return orderEntityMapper.toOrderModel(orderRepository.save(orderEntity));
    }

    @Override
    public boolean hasActiveOrders(Long customerId) {
        return orderRepository.countByCustomerIdAndStatusIn(
                customerId,
                List.of(OrderStatus.PENDING, OrderStatus.IN_PREPARATION, OrderStatus.READY)
        ) > 0;
    }

    @Override
    public Page<OrderModel> findByRestaurantIdAndStatus(Long restaurantId, OrderStatus status, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return (status == null || status.toString().isEmpty())
                ? orderRepository.findByRestaurantId(restaurantId, pageRequest).map(orderEntityMapper::toOrderModel)
                : orderRepository.findByRestaurantIdAndStatus(restaurantId, status, pageRequest).map(orderEntityMapper::toOrderModel);
    }
}
