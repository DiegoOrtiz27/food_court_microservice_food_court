package com.foodquart.microservicefoodcourt.infrastructure.out.jpa.mapper;

import com.foodquart.microservicefoodcourt.domain.model.OrderItemModel;
import com.foodquart.microservicefoodcourt.domain.model.OrderModel;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.entity.DishEntity;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.entity.OrderEntity;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.entity.OrderItemEntity;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.entity.RestaurantEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

public class OrderMapperHelper {
    @AfterMapping
    public static void linkItemsToOrder(@MappingTarget OrderEntity orderEntity, OrderModel orderModel) {
        if (orderEntity.getItems() != null && orderModel.getItems() != null) {
            for (int i = 0; i < orderEntity.getItems().size(); i++) {
                OrderItemEntity itemEntity = orderEntity.getItems().get(i);
                OrderItemModel itemModel = orderModel.getItems().get(i);

                itemEntity.setOrder(orderEntity);

                DishEntity dish = new DishEntity();
                dish.setId(itemModel.getDishId());
                itemEntity.setDish(dish);
            }
        }
    }

    @AfterMapping
    public static void restaurantToOrder(@MappingTarget OrderEntity orderEntity, OrderModel orderModel) {
        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setId(orderModel.getRestaurantId());
        orderEntity.setRestaurant(restaurant);
    }
}
