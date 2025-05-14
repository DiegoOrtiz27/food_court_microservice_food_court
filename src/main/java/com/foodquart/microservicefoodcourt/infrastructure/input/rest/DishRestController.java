package com.foodquart.microservicefoodcourt.infrastructure.input.rest;

import com.foodquart.microservicefoodcourt.application.dto.request.DishRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.request.EnableDishRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.request.UpdateDishRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.response.DishListResponseDto;
import com.foodquart.microservicefoodcourt.application.dto.response.DishResponseDto;
import com.foodquart.microservicefoodcourt.application.handler.IDishHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dishes")
@RequiredArgsConstructor
public class DishRestController {
    private final IDishHandler dishHandler;

    @Operation(summary = "Create a new dish")
    @ApiResponse(responseCode = "201", description = "Dish created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "403", description = "Only restaurant owners can create dishes")
    @PostMapping("/")
    public ResponseEntity<DishResponseDto> createDish(
            @Valid @RequestBody DishRequestDto dishRequestDto) {
        return ResponseEntity.ok(dishHandler.createDish(dishRequestDto));
    }

    @Operation(summary = "Update a dish (price and description only)")
    @ApiResponse(responseCode = "204", description = "Dish updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid data")
    @ApiResponse(responseCode = "403", description = "Not authorized")
    @PatchMapping("/{id}")
    public ResponseEntity<DishResponseDto> updateDish(
            @PathVariable("id") Long dishId,
            @Valid @RequestBody UpdateDishRequestDto updateDishRequestDto) {
        return ResponseEntity.ok(dishHandler.updateDish(dishId, updateDishRequestDto));
    }

    @Operation(summary = "Enable or disable a dish")
    @ApiResponse(responseCode = "204", description = "Dish status updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "403", description = "Not authorized")
    @PatchMapping("/status/{id}")
    public ResponseEntity<DishResponseDto> enableOrDisableDish(
            @PathVariable("id") Long dishId,
            @Valid @RequestBody EnableDishRequestDto enableDishRequestDto) {
        return ResponseEntity.ok(dishHandler.enableOrDisableDish(dishId, enableDishRequestDto));
    }

    @Operation(summary = "Get dishes by restaurant with pagination and category filter")
    @ApiResponse(responseCode = "200", description = "Dishes retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Restaurant not found")
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<Page<DishListResponseDto>> getDishesByRestaurant(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(dishHandler.getDishesByRestaurant(restaurantId, category, page, size));
    }
}
