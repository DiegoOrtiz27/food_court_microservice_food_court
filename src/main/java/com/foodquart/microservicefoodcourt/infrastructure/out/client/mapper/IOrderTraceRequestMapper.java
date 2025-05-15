package com.foodquart.microservicefoodcourt.infrastructure.out.client.mapper;

import com.foodquart.microservicefoodcourt.domain.model.OrderTraceModel;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.dto.request.OrderTraceRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderTraceRequestMapper {

    OrderTraceRequestDto toMapper(OrderTraceModel orderTraceModel);
}