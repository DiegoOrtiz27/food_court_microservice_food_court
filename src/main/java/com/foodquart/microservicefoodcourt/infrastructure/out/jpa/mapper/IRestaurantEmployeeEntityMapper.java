package com.foodquart.microservicefoodcourt.infrastructure.out.jpa.mapper;

import com.foodquart.microservicefoodcourt.domain.model.RestaurantEmployeeModel;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.entity.RestaurantEmployeeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IRestaurantEmployeeEntityMapper {

    @Mapping(target = "restaurant.id", source = "restaurantId")
    RestaurantEmployeeEntity toEntity(RestaurantEmployeeModel restaurantEmployeeModel);

    @Mapping(target = "restaurantId", source = "restaurant.id")
    RestaurantEmployeeModel toRestaurantEmployeeModel(RestaurantEmployeeEntity restaurantEmployeeEntity);
}
