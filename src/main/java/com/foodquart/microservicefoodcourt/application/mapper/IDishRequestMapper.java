package com.foodquart.microservicefoodcourt.application.mapper;

import com.foodquart.microservicefoodcourt.application.dto.request.DishRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.request.EnableDishRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.request.UpdateDishRequestDto;
import com.foodquart.microservicefoodcourt.domain.model.DishModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IDishRequestMapper {

    DishModel toDish(DishRequestDto dishRequestDto);

    DishModel toDish(UpdateDishRequestDto updateDishRequestDto);

    DishModel toDish(EnableDishRequestDto enableDishRequestDto);
}
