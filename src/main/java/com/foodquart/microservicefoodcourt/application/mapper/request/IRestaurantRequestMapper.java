package com.foodquart.microservicefoodcourt.application.mapper.request;

import com.foodquart.microservicefoodcourt.application.dto.request.RestaurantRequestDto;
import com.foodquart.microservicefoodcourt.domain.model.RestaurantModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestaurantRequestMapper {

    RestaurantModel toRestaurant(RestaurantRequestDto restaurantRequestDto);
}
