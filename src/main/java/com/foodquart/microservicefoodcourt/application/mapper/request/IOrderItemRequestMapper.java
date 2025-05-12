package com.foodquart.microservicefoodcourt.application.mapper.request;

import com.foodquart.microservicefoodcourt.application.dto.request.OrderItemRequestDto;
import com.foodquart.microservicefoodcourt.domain.model.OrderItemModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderItemRequestMapper {

    @Mapping(target = "dish.id", source = "dishId")
    OrderItemModel toOrderItem(OrderItemRequestDto orderItemRequestDto);
}
