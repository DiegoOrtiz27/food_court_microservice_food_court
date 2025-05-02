package com.foodquart.microservicefoodcourt.infrastructure.out.jpa.mapper;

import com.foodquart.microservicefoodcourt.domain.model.DishModel;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.entity.DishEntity;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.entity.RestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IDishEntityMapper {

    @Mapping(target = "restaurant.id", source = "restaurantId")
    DishEntity toEntity(DishModel dish);

    @Mapping(target = "restaurantId", source = "restaurant.id")
    DishModel toDishModel(DishEntity dishEntity);
}
