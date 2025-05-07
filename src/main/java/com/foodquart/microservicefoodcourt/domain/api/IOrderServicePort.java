package com.foodquart.microservicefoodcourt.domain.api;

import com.foodquart.microservicefoodcourt.domain.model.OrderModel;

public interface IOrderServicePort {

    OrderModel createOrder(OrderModel orderModel);
}
