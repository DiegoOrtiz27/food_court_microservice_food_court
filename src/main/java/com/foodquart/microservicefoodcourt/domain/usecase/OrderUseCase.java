package com.foodquart.microservicefoodcourt.domain.usecase;

import com.foodquart.microservicefoodcourt.domain.api.IOrderServicePort;
import com.foodquart.microservicefoodcourt.domain.exception.DomainException;
import com.foodquart.microservicefoodcourt.domain.model.*;
import com.foodquart.microservicefoodcourt.domain.spi.*;
import com.foodquart.microservicefoodcourt.domain.util.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.foodquart.microservicefoodcourt.domain.util.DishMessages.DISH_NOT_ACTIVE;
import static com.foodquart.microservicefoodcourt.domain.util.DishMessages.DISH_NOT_FROM_RESTAURANT;
import static com.foodquart.microservicefoodcourt.domain.util.OrderMessages.*;
import static com.foodquart.microservicefoodcourt.domain.util.ValidationUtil.*;

public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort;
    private final IUserClientPort userClientPort;
    private final IMessagingClientPort messagingClientPort;
    private final ISecurityContextPort securityContextPort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, IDishPersistencePort dishPersistencePort, IRestaurantPersistencePort restaurantPersistencePort, IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort, IUserClientPort userClientPort, IMessagingClientPort messagingClientPort, ISecurityContextPort securityContextPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.restaurantEmployeePersistencePort = restaurantEmployeePersistencePort;
        this.userClientPort = userClientPort;
        this.messagingClientPort = messagingClientPort;
        this.securityContextPort = securityContextPort;
    }

    @Override
    public OrderModel createOrder(OrderModel orderModel) {
        orderModel.setCustomerId(securityContextPort.getCurrentUserId());

        validateOrderCreation(orderModel);
        existsRestaurantById(restaurantPersistencePort, orderModel.getRestaurantId());

        if (orderPersistencePort.hasActiveOrders(orderModel.getCustomerId(), OrderStatus.getActiveStatuses())) {
            throw new DomainException(CUSTOMER_HAS_ACTIVE_ORDER);
        }

        for (OrderItemModel item : orderModel.getItems()) {
            Long dishId = item.getDish().getId();
            DishModel dish = validateExistingDish(dishPersistencePort, dishId);

            if (Boolean.FALSE.equals(dish.getActive())) {
                throw new DomainException(String.format(DISH_NOT_ACTIVE, dishId));
            }

            if (!dish.getRestaurantId().equals(orderModel.getRestaurantId())) {
                throw new DomainException(DISH_NOT_FROM_RESTAURANT);
            }
        }

        orderModel.setStatus(OrderStatus.PENDING);
        LocalDateTime now = LocalDateTime.now();
        orderModel.setCreatedAt(now);
        orderModel.setUpdatedAt(now);
        return orderPersistencePort.saveOrder(orderModel);
    }

    @Override
    public Page<OrderModel> getOrdersByRestaurant(Long restaurantId, OrderStatus status, int page, int size) {
        Long employeeId = securityContextPort.getCurrentUserId();
        validateRestaurant(restaurantId, employeeId);
        return orderPersistencePort.findByRestaurantIdAndStatus(restaurantId, status, page, size);
    }

    @Override
    public OrderModel assignOrderToEmployee(Long orderId) {
        Long employeeId = securityContextPort.getCurrentUserId();

        validateUpdateEmployeeFields(orderId, employeeId);
        OrderModel existingOrder = existingOrder(orderId);
        validateRestaurant(existingOrder.getRestaurantId(), employeeId);

        if (!existingOrder.getStatus().equals(OrderStatus.PENDING)) {
            throw new DomainException(ORDER_IS_NOT_PENDING);
        }

        if(!existingOrder.getStatus().isNotAssignedStatus()) {
            throw new DomainException(ORDER_ALREADY_ASSIGNED);
        }

        existingOrder.setStatus(OrderStatus.IN_PREPARATION);
        existingOrder.setAssignedEmployeeId(employeeId);
        existingOrder.setUpdatedAt(LocalDateTime.now());
        return orderPersistencePort.saveOrder(existingOrder);
    }

    @Override
    public OrderModel notifyOrderReady(Long orderId) {
        Long employeeId = securityContextPort.getCurrentUserId();

        validateUpdateEmployeeFields(orderId, employeeId);
        OrderModel existingOrder = existingOrder(orderId);
        validateRestaurant(existingOrder.getRestaurantId(), employeeId);

        if(!existingOrder.getStatus().equals(OrderStatus.IN_PREPARATION)) {
            throw new DomainException(ORDER_IS_NOT_PREPARING);
        }

        if (!orderPersistencePort.hasAssignedOrder(employeeId, orderId)) {
            throw new DomainException(String.format(EMPLOYEE_WITH_NOT_ORDER_ASSOCIATED, employeeId, orderId));
        }

        String securityPin = GenericMethods.generateSecurityPin();

        CustomerModel customerModel = userClientPort.getUserInfo(existingOrder.getCustomerId());
        boolean notificationSuccess = messagingClientPort.notifyOrderReady(new NotificationModel(customerModel.getPhone(), String.format(ORDER_PIN, orderId, securityPin)));

        if (notificationSuccess) {
            existingOrder.setStatus(OrderStatus.READY);
            existingOrder.setAssignedEmployeeId(employeeId);
            existingOrder.setSecurityPin(securityPin);
            existingOrder.setUpdatedAt(LocalDateTime.now());
            return orderPersistencePort.saveOrder(existingOrder);
        } else {
            throw new DomainException(NOTIFICATION_NOT_SENT);
        }
    }

    @Override
    public OrderModel markOrderAsDelivered(Long orderId, String securityPin) {
        Long employeeId = securityContextPort.getCurrentUserId();

        validateDeliveryFields(orderId, employeeId, securityPin);
        OrderModel existingOrder = existingOrder(orderId);
        validateRestaurant(existingOrder.getRestaurantId(), employeeId);

        if (!existingOrder.getStatus().equals(OrderStatus.READY)) {
            throw new DomainException(ORDER_NOT_READY_FOR_DELIVERY);
        }

        validateSecurityPin(securityPin, existingOrder.getSecurityPin());

        existingOrder.setStatus(OrderStatus.DELIVERED);
        existingOrder.setUpdatedAt(LocalDateTime.now());

        return orderPersistencePort.saveOrder(existingOrder);
    }

    @Override
    public OrderModel cancelOrder(Long orderId) {
        Long customerId = securityContextPort.getCurrentUserId();

        validateUpdateCustomerFields(orderId, customerId);
        OrderModel existingOrder = existingOrder(orderId);

        if (!existingOrder.getCustomerId().equals(customerId)) {
            throw new DomainException(ORDER_NOT_BELONG_TO_CUSTOMER);
        }

        if (existingOrder.getStatus() != OrderStatus.PENDING) {
            throw new DomainException(ORDER_IS_NOT_PENDING);
        }

        existingOrder.setStatus(OrderStatus.CANCELLED);
        existingOrder.setUpdatedAt(LocalDateTime.now());

        return orderPersistencePort.saveOrder(existingOrder);
    }

    public OrderModel existingOrder(Long orderId) {
        Optional<OrderModel> existingOrder = orderPersistencePort.findById(orderId);

        if(existingOrder.isEmpty()) {
            throw new DomainException(String.format(ORDER_NOT_FOUND, orderId));
        }

        return existingOrder.get();
    }

    public void validateRestaurant(Long restaurantId, Long employeeId) {
        existsRestaurantById(restaurantPersistencePort, restaurantId);
        existsByEmployeeIdAndRestaurantId(restaurantEmployeePersistencePort, restaurantId, employeeId);
    }

}
