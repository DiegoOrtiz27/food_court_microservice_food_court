package com.foodquart.microservicefoodcourt.domain.api;

import com.foodquart.microservicefoodcourt.domain.model.OrderModel;
import com.foodquart.microservicefoodcourt.domain.util.OrderStatus;
import com.foodquart.microservicefoodcourt.domain.util.Pagination;

public interface IOrderServicePort {

    OrderModel createOrder(OrderModel orderModel);

    Pagination<OrderModel> getOrdersByRestaurant(Long restaurantId, OrderStatus status, int page, int size);

    OrderModel assignOrderToEmployee(Long orderId);

    OrderModel notifyOrderReady(Long orderId);

    OrderModel markOrderAsDelivered(Long orderId, String securityPin);

    OrderModel cancelOrder(Long orderId);

}
