package com.foodquart.microservicefoodcourt.application.mapper;

import com.foodquart.microservicefoodcourt.application.dto.response.OrderItemResponseDto;
import com.foodquart.microservicefoodcourt.domain.model.OrderItemModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderItemResponseMapper {
    @Mapping(target = "dishName", source = "dish.name")
    @Mapping(target = "price", source = "dish.price")
    OrderItemResponseDto toDto(OrderItemModel item);
}
