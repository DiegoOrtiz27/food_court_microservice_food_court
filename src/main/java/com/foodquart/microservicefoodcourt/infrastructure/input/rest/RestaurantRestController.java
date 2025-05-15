package com.foodquart.microservicefoodcourt.infrastructure.input.rest;

import com.foodquart.microservicefoodcourt.application.dto.response.PaginationListResponseDto;
import com.foodquart.microservicefoodcourt.application.dto.response.RestaurantItemResponse;
import com.foodquart.microservicefoodcourt.application.dto.response.RestaurantResponseDto;
import com.foodquart.microservicefoodcourt.application.handler.IRestaurantHandler;
import com.foodquart.microservicefoodcourt.application.dto.request.RestaurantRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.foodquart.microservicefoodcourt.infrastructure.documentation.APIRestaurantDocumentationConstant.*;
import static com.foodquart.microservicefoodcourt.infrastructure.documentation.ResponseCode.*;

@RestController
@RequestMapping("/api/v1/restaurant")
@RequiredArgsConstructor
public class RestaurantRestController {

    private final IRestaurantHandler restaurantHandler;

    @PostMapping("/")
    @Operation(summary = CREATE_RESTAURANT_SUMMARY)
    @ApiResponse(responseCode = CODE_201, description = CREATE_RESTAURANT_SUCCESS_DESCRIPTION)
    @ApiResponse(responseCode = CODE_400, description = CREATE_RESTAURANT_VALIDATION_ERROR_DESCRIPTION)
    @ApiResponse(responseCode = CODE_409, description = CREATE_RESTAURANT_OWNER_NOT_VALID_DESCRIPTION)
    public ResponseEntity<RestaurantResponseDto> createRestaurant(@Valid @RequestBody RestaurantRequestDto requestDto) {
        return ResponseEntity.ok(restaurantHandler.saveRestaurant(requestDto));
    }

    @GetMapping("/")
    @Operation(summary = GET_ALL_RESTAURANTS_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description = GET_ALL_RESTAURANTS_SUCCESS_DESCRIPTION)
    @ApiResponse(responseCode = CODE_204, description = GET_ALL_RESTAURANTS_NO_CONTENT_DESCRIPTION)
    public ResponseEntity<PaginationListResponseDto<RestaurantItemResponse>>  getAllRestaurants(
            @Parameter(description = GET_ALL_RESTAURANTS_PAGE_PARAMETER_DESCRIPTION, example = GET_ALL_RESTAURANTS_PAGE_PARAMETER_EXAMPLE)
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = GET_ALL_RESTAURANTS_SIZE_PARAMETER_DESCRIPTION, example = GET_ALL_RESTAURANTS_SIZE_PARAMETER_EXAMPLE)
            @RequestParam(defaultValue = "10") int size) {

        PaginationListResponseDto<RestaurantItemResponse> response = restaurantHandler.getAllRestaurants(page, size);

        if (response.getContent().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(response);
    }
}

