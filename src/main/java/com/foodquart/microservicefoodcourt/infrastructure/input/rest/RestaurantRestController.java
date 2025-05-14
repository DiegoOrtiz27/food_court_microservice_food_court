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

@RestController
@RequestMapping("/api/v1/restaurant")
@RequiredArgsConstructor
public class RestaurantRestController {

    private final IRestaurantHandler restaurantHandler;

    @Operation(summary = "Create a new restaurant")
    @ApiResponse(responseCode = "201", description = "Restaurant created successfully")
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "409", description = "Owner is not valid")
    @PostMapping("/")
    public ResponseEntity<RestaurantResponseDto> createRestaurant(@Valid @RequestBody RestaurantRequestDto requestDto) {
        return ResponseEntity.ok(restaurantHandler.saveRestaurant(requestDto));
    }

    @Operation(summary = "Get all restaurants paginated and sorted by name")
    @ApiResponse(responseCode = "200", description = "Restaurants retrieved successfully")
    @ApiResponse(responseCode = "204", description = "No restaurants found")
    @GetMapping("/")
    public ResponseEntity<PaginationListResponseDto<RestaurantItemResponse>>  getAllRestaurants(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size) {

        PaginationListResponseDto<RestaurantItemResponse> response = restaurantHandler.getAllRestaurants(page, size);

        if (response.getContent().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(response);
    }
}
