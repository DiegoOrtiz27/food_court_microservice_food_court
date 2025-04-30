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

    @Mapping(target = "restaurant", source = "restaurantId", qualifiedByName = "idToRestaurant")
    DishEntity toEntity(DishModel model);

    @Mapping(target = "restaurantId", source = "restaurant.id")
    DishModel toDishModel(DishEntity entity);

    @Named("idToRestaurant")
    default RestaurantEntity idToRestaurant(Long restaurantId) {
        if (restaurantId == null) return null;
        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setId(restaurantId);
        return restaurant;
    }
}
