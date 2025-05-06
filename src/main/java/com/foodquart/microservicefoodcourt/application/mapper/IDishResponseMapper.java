package com.foodquart.microservicefoodcourt.application.mapper;

import com.foodquart.microservicefoodcourt.application.dto.DishListResponseDto;
import com.foodquart.microservicefoodcourt.application.dto.DishResponseDto;
import com.foodquart.microservicefoodcourt.domain.model.DishModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IDishResponseMapper {
    DishResponseDto toResponse(Long dishId, String response);

    DishListResponseDto toResponse(DishModel dishModel);
}
