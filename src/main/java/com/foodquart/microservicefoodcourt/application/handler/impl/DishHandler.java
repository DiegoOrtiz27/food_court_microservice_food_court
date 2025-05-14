package com.foodquart.microservicefoodcourt.application.handler.impl;

import com.foodquart.microservicefoodcourt.application.dto.request.DishRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.request.EnableDishRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.request.UpdateDishRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.response.DishListResponseDto;
import com.foodquart.microservicefoodcourt.application.dto.response.DishResponseDto;
import com.foodquart.microservicefoodcourt.application.dto.response.PaginationListResponseDto;
import com.foodquart.microservicefoodcourt.application.handler.IDishHandler;
import com.foodquart.microservicefoodcourt.application.mapper.request.IDishRequestMapper;
import com.foodquart.microservicefoodcourt.application.mapper.response.IDishResponseMapper;
import com.foodquart.microservicefoodcourt.domain.api.IDishServicePort;
import com.foodquart.microservicefoodcourt.domain.model.DishModel;
import com.foodquart.microservicefoodcourt.domain.util.Pagination;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.foodquart.microservicefoodcourt.domain.util.DishMessages.*;

@Service
@RequiredArgsConstructor
@Transactional
public class DishHandler implements IDishHandler {

    private final IDishServicePort dishServicePort;
    private final IDishRequestMapper dishRequestMapper;
    private final IDishResponseMapper dishResponseMapper;

    @Override
    public DishResponseDto createDish(DishRequestDto dishRequestDto) {
        DishModel dishModel = dishRequestMapper.toDish(dishRequestDto);
        dishModel = dishServicePort.createDish(dishModel);
        return dishResponseMapper.toResponse(dishModel.getId(), DISH_CREATED);
    }

    @Override
    public DishResponseDto updateDish(Long dishId, UpdateDishRequestDto updateDishRequestDto) {
        DishModel dishModel = dishRequestMapper.toDish(updateDishRequestDto);
        dishServicePort.updateDish(dishId, dishModel);
        return dishResponseMapper.toResponse(dishId, DISH_UPDATED);
    }

    @Override
    public DishResponseDto enableOrDisableDish(Long dishId, EnableDishRequestDto enableDishRequestDto) {
        DishModel dishModel = dishRequestMapper.toDish(enableDishRequestDto);
        dishServicePort.enableOrDisableDish(dishId, dishModel);
        String response = Boolean.TRUE.equals(enableDishRequestDto.getActive())
                ? DISH_ENABLED
                : DISH_DISABLED;
        return dishResponseMapper.toResponse(dishId, response);
    }

    @Override
    public PaginationListResponseDto<DishListResponseDto> getDishesByRestaurant(Long restaurantId, String category, int page, int size) {
        Pagination<DishModel> pagination = dishServicePort.getDishesByRestaurant(restaurantId, category, page, size);

        List<DishListResponseDto> items = pagination.getItems().stream()
                .map(dishResponseMapper::toResponse)
                .toList();

        return new PaginationListResponseDto<>(
                items,
                pagination.getPage(),
                pagination.getSize(),
                pagination.getTotalItems(),
                pagination.getTotalPages(),
                pagination.isHasNext(),
                pagination.isHasPrevious()
        );
    }
}