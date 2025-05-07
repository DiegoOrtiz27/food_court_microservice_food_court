package com.foodquart.microservicefoodcourt.domain.usecase;

import com.foodquart.microservicefoodcourt.domain.api.IOrderServicePort;
import com.foodquart.microservicefoodcourt.domain.exception.DomainException;
import com.foodquart.microservicefoodcourt.domain.model.DishModel;
import com.foodquart.microservicefoodcourt.domain.model.OrderItemModel;
import com.foodquart.microservicefoodcourt.domain.model.OrderModel;
import com.foodquart.microservicefoodcourt.domain.spi.IDishPersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IOrderPersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodquart.microservicefoodcourt.domain.util.*;

public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, IDishPersistencePort dishPersistencePort, IRestaurantPersistencePort restaurantPersistencePort) {
        this.orderPersistencePort = orderPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
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
            DishModel dish = dishPersistencePort.findById(item.getDishId())
                    .orElseThrow(() -> new DomainException(String.format(DishMessages.DISH_NOT_FOUND, item.getDishId())));

            if (Boolean.FALSE.equals(dish.getActive())) {
                throw new DomainException(String.format(DishMessages.DISH_NOT_ACTIVE, item.getDishId()));
            }

            if (!dish.getRestaurantId().equals(orderModel.getRestaurantId())) {
                throw new DomainException(DishMessages.DISH_NOT_FROM_RESTAURANT);
            }
        }

        orderModel.setStatus(OrderStatus.PENDING);

        return orderPersistencePort.saveOrder(orderModel);
    }
}
