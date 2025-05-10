package com.foodquart.microservicefoodcourt.application.mapper;

import com.foodquart.microservicefoodcourt.application.dto.request.OrderRequestDto;
import com.foodquart.microservicefoodcourt.domain.model.OrderModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        uses = IOrderItemRequestMapper.class)
public interface IOrderRequestMapper {

    @Mapping(target = "items", source = "items")
    OrderModel toOrder(OrderRequestDto orderRequestDto);
}
