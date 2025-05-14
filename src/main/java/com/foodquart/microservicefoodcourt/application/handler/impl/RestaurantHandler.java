package com.foodquart.microservicefoodcourt.application.handler.impl;

import com.foodquart.microservicefoodcourt.application.dto.response.PaginationListResponseDto;
import com.foodquart.microservicefoodcourt.application.dto.response.RestaurantItemResponse;
import com.foodquart.microservicefoodcourt.application.dto.response.RestaurantResponseDto;
import com.foodquart.microservicefoodcourt.application.handler.IRestaurantHandler;
import com.foodquart.microservicefoodcourt.application.mapper.request.IRestaurantRequestMapper;
import com.foodquart.microservicefoodcourt.application.dto.request.RestaurantRequestDto;
import com.foodquart.microservicefoodcourt.application.mapper.response.IRestaurantResponseMapper;
import com.foodquart.microservicefoodcourt.domain.api.IRestaurantServicePort;
import com.foodquart.microservicefoodcourt.domain.model.RestaurantModel;
import com.foodquart.microservicefoodcourt.domain.util.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

import static com.foodquart.microservicefoodcourt.domain.util.RestaurantMessages.RESTAURANT_CREATED;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantHandler implements IRestaurantHandler {

    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantRequestMapper restaurantRequestMapper;
    private final IRestaurantResponseMapper restaurantResponseMapper;

    @Override
    public RestaurantResponseDto saveRestaurant(RestaurantRequestDto restaurantRequestDto) {
        RestaurantModel restaurantModel = restaurantRequestMapper.toRestaurant(restaurantRequestDto);
        restaurantModel = restaurantServicePort.saveRestaurant(restaurantModel);
        return restaurantResponseMapper.toResponse(restaurantModel.getId(), RESTAURANT_CREATED);
    }

    @Override
    public PaginationListResponseDto<RestaurantItemResponse> getAllRestaurants(int page, int size) {
        Pagination<RestaurantModel> pagination = restaurantServicePort.getAllRestaurants(page, size);

        List<RestaurantItemResponse> items = pagination.getItems().stream()
                .map(restaurantResponseMapper::toResponse)
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
