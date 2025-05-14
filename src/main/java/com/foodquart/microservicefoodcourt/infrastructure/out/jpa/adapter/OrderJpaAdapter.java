package com.foodquart.microservicefoodcourt.infrastructure.out.jpa.adapter;

import com.foodquart.microservicefoodcourt.domain.model.OrderModel;
import com.foodquart.microservicefoodcourt.domain.spi.IOrderPersistencePort;
import com.foodquart.microservicefoodcourt.domain.util.OrderStatus;
import com.foodquart.microservicefoodcourt.domain.util.Pagination;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.entity.OrderEntity;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.repository.IOrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class OrderJpaAdapter implements IOrderPersistencePort {

    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;
    private final EntityManager entityManager;

    @Override
    public OrderModel saveOrder(OrderModel orderModel) {
        OrderEntity orderEntity = orderEntityMapper.toEntity(orderModel);
        return orderEntityMapper.toOrderModel(orderRepository.save(orderEntity));
    }


    @Override
    public boolean hasActiveOrders(Long customerId, List<OrderStatus> statuses) {
        return orderRepository.countByCustomerIdAndStatusIn(
                customerId,
                statuses
        ) > 0;
    }

    @Override
    public Pagination<OrderModel> findByRestaurantIdAndStatus(Long restaurantId, OrderStatus status, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<OrderEntity> resultPage = (status == null || status.toString().isEmpty())
                ? orderRepository.findByRestaurantId(restaurantId, pageRequest)
                : orderRepository.findByRestaurantIdAndStatus(restaurantId, status, pageRequest);

        List<OrderModel> content = resultPage.getContent()
                .stream()
                .map(orderEntityMapper::toOrderModel)
                .collect(Collectors.toList());

        return new Pagination<>(
                content,
                page,
                size,
                getTotalOrdersCount(restaurantId, status)
        );
    }

    @Override
    public Optional<OrderModel> findById(Long id) {
        return orderRepository.findById(id)
                .map(orderEntityMapper::toOrderModel);
    }

    @Override
    public boolean hasAssignedOrder(Long employeeId, Long orderId) {
         return orderRepository.countByEmployeeIdAndOrderId(
                employeeId,
                orderId
        ) > 0;
    }

    private long getTotalOrdersCount(Long restaurantId, OrderStatus status) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<OrderEntity> root = countQuery.from(OrderEntity.class);

        Predicate restaurantPredicate = cb.equal(root.get("restaurant").get("id"), restaurantId);

        if (status != null) {
            Predicate statusPredicate = cb.equal(root.get("status"), status);
            countQuery.where(cb.and(restaurantPredicate, statusPredicate));
        } else {
            countQuery.where(restaurantPredicate);
        }

        countQuery.select(cb.count(root));
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
