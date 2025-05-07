package com.foodquart.microservicefoodcourt.infrastructure.out.jpa.mapper;

import com.foodquart.microservicefoodcourt.domain.model.OrderModel;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.entity.OrderEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderEntityMapper {

    @Mapping(target = "restaurantId", source = "restaurant.id")
    OrderModel toModel(OrderEntity entity);

    @AfterMapping
    default void afterMapping(@MappingTarget OrderEntity orderEntity, OrderModel orderModel) {
        OrderMapperHelper.linkItemsToOrder(orderEntity, orderModel);
        OrderMapperHelper.restaurantToOrder(orderEntity, orderModel);
    }
    OrderEntity toEntity(OrderModel model);
}
