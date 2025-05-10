package com.foodquart.microservicefoodcourt.infrastructure.out.client.mapper;

import com.foodquart.microservicefoodcourt.domain.model.RestaurantEmployeeModel;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.dto.request.CreateEmployeeRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IUserRequestMapper {
    CreateEmployeeRequestDto toMapper(RestaurantEmployeeModel restaurantEmployeeModel);

}
