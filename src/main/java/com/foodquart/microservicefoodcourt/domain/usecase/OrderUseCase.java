package com.foodquart.microservicefoodcourt.domain.usecase;

import com.foodquart.microservicefoodcourt.domain.api.IOrderServicePort;
import com.foodquart.microservicefoodcourt.domain.exception.DomainException;
import com.foodquart.microservicefoodcourt.domain.model.DishModel;
import com.foodquart.microservicefoodcourt.domain.model.OrderItemModel;
import com.foodquart.microservicefoodcourt.domain.model.OrderModel;
import com.foodquart.microservicefoodcourt.domain.spi.IDishPersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IOrderPersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantEmployeePersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodquart.microservicefoodcourt.domain.util.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, IDishPersistencePort dishPersistencePort, IRestaurantPersistencePort restaurantPersistencePort, IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort) {
        this.orderPersistencePort = orderPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.restaurantEmployeePersistencePort = restaurantEmployeePersistencePort;
    }

    @Override
    public OrderModel createOrder(OrderModel orderModel) {
        ValidationUtil.validateOrderCreation(orderModel);

        if (!restaurantPersistencePort.existsById(orderModel.getRestaurantId())) {
            throw new DomainException(String.format(RestaurantMessages.RESTAURANT_NOT_FOUND, orderModel.getRestaurantId()));
        }

        if (orderPersistencePort.hasActiveOrders(orderModel.getCustomerId())) {
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
        if (!restaurantPersistencePort.existsById(restaurantId)) {
            throw new DomainException(String.format(RestaurantMessages.RESTAURANT_NOT_FOUND, restaurantId));
        }

        if (!restaurantEmployeePersistencePort.existsByEmployeeIdAndRestaurantId(employeeId, restaurantId)) {
            throw new DomainException(String.format(RestaurantMessages.EMPLOYEE_NOT_ASSOCIATED_TO_RESTAURANT, restaurantId));
        }
        return orderPersistencePort.findByRestaurantIdAndStatus(restaurantId, status, page, size);
    }
}
