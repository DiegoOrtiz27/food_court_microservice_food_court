package com.foodquart.microservicefoodcourt.application.mapper;

import com.foodquart.microservicefoodcourt.application.dto.response.OrderListResponseDto;
import com.foodquart.microservicefoodcourt.application.dto.response.OrderResponseDto;
import com.foodquart.microservicefoodcourt.domain.model.OrderModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        uses = IOrderItemResponseMapper.class)
public interface IOrderResponseMapper {
    OrderResponseDto toResponse(Long orderId, String status, String response);

    @Mapping(target = "items", source = "items")
    OrderListResponseDto toResponse(OrderModel orderModel);
}
