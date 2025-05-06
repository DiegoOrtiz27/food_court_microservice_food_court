package com.foodquart.microservicefoodcourt.application.mapper;

import com.foodquart.microservicefoodcourt.application.dto.CreateRestaurantEmployeeRequestDto;
import com.foodquart.microservicefoodcourt.domain.model.RestaurantEmployeeModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestaurantEmployeeRequestMapper {

    RestaurantEmployeeModel toRestaurantEmployee(CreateRestaurantEmployeeRequestDto createRestaurantEmployeeRequestDto);

}
