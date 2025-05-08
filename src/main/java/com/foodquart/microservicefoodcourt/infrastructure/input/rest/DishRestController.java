package com.foodquart.microservicefoodcourt.infrastructure.input.rest;

import com.foodquart.microservicefoodcourt.application.dto.*;
import com.foodquart.microservicefoodcourt.application.handler.IDishHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Long id = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(dishHandler.createDish(dishRequestDto, id));
    }

    @Operation(summary = "Update a dish (price and description only)")
    @ApiResponse(responseCode = "204", description = "Dish updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid data")
    @ApiResponse(responseCode = "403", description = "Not authorized")
    @PatchMapping("/{id}")
    public ResponseEntity<DishResponseDto> updateDish(
            @PathVariable("id") Long dishId,
            @Valid @RequestBody UpdateDishRequestDto updateDishRequestDto) {

        Long id = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        updateDishRequestDto.setId(dishId);
        return ResponseEntity.ok(dishHandler.updateDish(updateDishRequestDto, id));
    }

    @Operation(summary = "Enable or disable a dish")
    @ApiResponse(responseCode = "204", description = "Dish status updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "403", description = "Not authorized")
    @PatchMapping("/status/{id}")
    public ResponseEntity<DishResponseDto> enableOrDisableDish(
            @PathVariable("id") Long dishId,
            @Valid @RequestBody EnableDishRequestDto enableDishRequestDto) {
        Long id = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        enableDishRequestDto.setId(dishId);
        return ResponseEntity.ok(dishHandler.enableOrDisableDish(enableDishRequestDto, id));
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
