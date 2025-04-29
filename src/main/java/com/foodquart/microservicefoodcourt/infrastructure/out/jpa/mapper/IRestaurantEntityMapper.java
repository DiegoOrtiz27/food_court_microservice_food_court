package com.foodquart.microservicefoodcourt.infrastructure.out.jpa.mapper;

import com.foodquart.microservicefoodcourt.domain.model.RestaurantModel;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.entity.RestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IRestaurantEntityMapper {

    RestaurantEntity toEntity(RestaurantModel model);

    RestaurantModel toRestaurantModel(RestaurantEntity entity);
}
