package com.foodquart.microservicefoodcourt.domain.usecase;

import com.foodquart.microservicefoodcourt.domain.api.IOrderServicePort;
import com.foodquart.microservicefoodcourt.domain.exception.DomainException;
import com.foodquart.microservicefoodcourt.domain.model.*;
import com.foodquart.microservicefoodcourt.domain.spi.*;
import com.foodquart.microservicefoodcourt.domain.util.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

public class OrderUseCase implements IOrderServicePort {

    private final Random random = new Random();

    private final IOrderPersistencePort orderPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort;
    private final IUserClientPort userClientPort;
    private final IMessagingClientPort messagingClientPort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, IDishPersistencePort dishPersistencePort, IRestaurantPersistencePort restaurantPersistencePort, IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort, IUserClientPort userClientPort, IMessagingClientPort messagingClientPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.restaurantEmployeePersistencePort = restaurantEmployeePersistencePort;
        this.userClientPort = userClientPort;
        this.messagingClientPort = messagingClientPort;
    }

    @Override
    public OrderModel createOrder(OrderModel orderModel) {
        ValidationUtil.validateOrderCreation(orderModel);

        if (!restaurantPersistencePort.existsById(orderModel.getRestaurantId())) {
            throw new DomainException(String.format(RestaurantMessages.RESTAURANT_NOT_FOUND, orderModel.getRestaurantId()));
        }

        if (orderPersistencePort.hasActiveOrders(orderModel.getCustomerId(), OrderStatus.getActiveStatuses())) {
            throw new DomainException(OrderMessages.CUSTOMER_HAS_ACTIVE_ORDER);
        }

        for (OrderItemModel item : orderModel.getItems()) {
            Long dishId = item.getDish().getId();
            DishModel dish = dishPersistencePort.findById(dishId)
                    .orElseThrow(() -> new DomainException(String.format(DishMessages.DISH_NOT_FOUND, dishId)));

            if (Boolean.FALSE.equals(dish.getActive())) {
                throw new DomainException(String.format(DishMessages.DISH_NOT_ACTIVE, dishId));
            }

            if (!dish.getRestaurantId().equals(orderModel.getRestaurantId())) {
                throw new DomainException(DishMessages.DISH_NOT_FROM_RESTAURANT);
            }
        }

        orderModel.setStatus(OrderStatus.PENDING);
        LocalDateTime now = LocalDateTime.now();
        orderModel.setCreatedAt(now);
        orderModel.setUpdatedAt(now);
        return orderPersistencePort.saveOrder(orderModel);
    }

    @Override
    public Page<OrderModel> getOrdersByRestaurant(Long employeeId, Long restaurantId, OrderStatus status, int page, int size) {
        validationOrder(restaurantId, employeeId);
        return orderPersistencePort.findByRestaurantIdAndStatus(restaurantId, status, page, size);
    }

    @Override
    public OrderModel assignOrderToEmployee(Long orderId, Long employeeId) {
        Optional<OrderModel> existingOrder = existingOrder(orderId);

        return existingOrder.map(order -> {
            if(!order.getStatus().isNotAssignedStatus()) {
                throw new DomainException(OrderMessages.ORDER_ALREADY_ASSIGNED);
            }

            validationOrder(order.getRestaurantId(), employeeId);

            order.setStatus(OrderStatus.IN_PREPARATION);
            order.setAssignedEmployeeId(employeeId);
            return orderPersistencePort.updateOrder(order);
        }).orElse(null);
    }

    @Override
    public OrderModel notifyOrderReady(Long orderId, Long employeeId) {
        Optional<OrderModel> existingOrder = existingOrder(orderId);

        return existingOrder.map(order -> {
            if(order.getStatus().equals(OrderStatus.IN_PREPARATION)) {
                validationOrder(order.getRestaurantId(), employeeId);

                if (!orderPersistencePort.hasAssignedOrder(employeeId, orderId)) {
                    throw new DomainException(String.format(OrderMessages.EMPLOYEE_WITH_NOT_ORDER_ASSOCIATED, employeeId, orderId));
                }

                String securityPin = generateSecurityPin();

                CustomerModel customerModel = userClientPort.getUserInfo(order.getCustomerId());
                boolean notificationSuccess = messagingClientPort.notifyOrderReady(new NotificationModel(customerModel.getPhone(), securityPin, orderId));

                if (notificationSuccess) {
                    order.setStatus(OrderStatus.READY);
                    order.setAssignedEmployeeId(employeeId);
                    order.setSecurityPin(securityPin);
                    return orderPersistencePort.updateOrder(order);
                } else {
                    throw new DomainException(OrderMessages.NOTIFICATION_NOT_SENT);
                }
            } else {
                throw new DomainException(OrderMessages.ORDER_IS_NOT_PREPARING);
            }
        }).orElse(null);
    }

    public Optional<OrderModel> existingOrder(Long orderId) {
        Optional<OrderModel> existingOrder = orderPersistencePort.findById(orderId);

        if(existingOrder.isEmpty()) {
            throw new DomainException(String.format(OrderMessages.ORDER_NOT_FOUND, orderId));
        }
        return existingOrder;
    }

    public void validationOrder(Long restaurantId, Long employeeId) {
        if (!restaurantPersistencePort.existsById(restaurantId)) {
            throw new DomainException(String.format(RestaurantMessages.RESTAURANT_NOT_FOUND, restaurantId));
        }

        if (!restaurantEmployeePersistencePort.existsByEmployeeIdAndRestaurantId(employeeId, restaurantId)) {
            throw new DomainException(String.format(RestaurantMessages.EMPLOYEE_NOT_ASSOCIATED_TO_RESTAURANT, restaurantId));
        }
    }

    private String generateSecurityPin() {
        return String.format("%04d", this.random.nextInt(10000));
    }

}
