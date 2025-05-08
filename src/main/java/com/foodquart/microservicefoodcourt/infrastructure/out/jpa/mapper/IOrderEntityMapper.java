package com.foodquart.microservicefoodcourt.infrastructure.out.jpa.mapper;

import com.foodquart.microservicefoodcourt.domain.model.OrderModel;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.entity.OrderEntity;
import org.mapstruct.*;


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderEntityMapper {

    @AfterMapping
    default void afterMapping(@MappingTarget OrderEntity orderEntity, OrderModel orderModel) {
        OrderMapperHelper.linkItemsToOrder(orderEntity, orderModel);
        OrderMapperHelper.restaurantToOrder(orderEntity, orderModel);
    }
    OrderEntity toEntity(OrderModel model);

    @Mapping(target = "restaurantId", source = "restaurant.id")
    @Mapping(target = "items", source = "items")
    OrderModel toOrderModel(OrderEntity entity);
}
