package com.foodquart.microservicefoodcourt.application.mapper;

import com.foodquart.microservicefoodcourt.application.dto.RestaurantListResponseDto;
import com.foodquart.microservicefoodcourt.application.dto.RestaurantResponseDto;
import com.foodquart.microservicefoodcourt.domain.model.RestaurantModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestaurantResponseMapper {
    RestaurantResponseDto toResponse(Long restaurantId, String response);

    RestaurantListResponseDto toResponse(RestaurantModel restaurantModel);
}
