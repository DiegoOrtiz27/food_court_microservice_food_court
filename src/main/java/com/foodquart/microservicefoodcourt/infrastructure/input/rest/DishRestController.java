package com.foodquart.microservicefoodcourt.infrastructure.input.rest;

import com.foodquart.microservicefoodcourt.application.dto.DishResponseDto;
import com.foodquart.microservicefoodcourt.application.dto.EnableDishRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.UpdateDishRequestDto;
import com.foodquart.microservicefoodcourt.application.handler.IDishHandler;
import com.foodquart.microservicefoodcourt.application.dto.DishRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dishes")
@RequiredArgsConstructor
public class DishRestController {
    private final IDishHandler dishHandler;

    @Operation(summary = "Create a new dish")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dish created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Only restaurant owners can create dishes")
    })
    @PostMapping("/")
    public ResponseEntity<DishResponseDto> createDish(
            @Valid @RequestBody DishRequestDto dishRequestDto) {
        Long id = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(dishHandler.createDish(dishRequestDto, id));
    }

    @Operation(summary = "Update a dish (price and description only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Dish updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "403", description = "Not authorized")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<DishResponseDto> updateDish(
            @PathVariable("id") Long dishId,
            @Valid @RequestBody UpdateDishRequestDto updateDishRequestDto) {

        Long id = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        updateDishRequestDto.setId(dishId);
        return ResponseEntity.ok(dishHandler.updateDish(updateDishRequestDto, id));
    }

    @Operation(summary = "Enable or disable a dish")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Dish status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Not authorized")
    })
    @PatchMapping("/status/{id}")
    public ResponseEntity<DishResponseDto> enableOrDisableDish(
            @PathVariable("id") Long dishId,
            @Valid @RequestBody EnableDishRequestDto enableDishRequestDto) {
        Long id = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        enableDishRequestDto.setId(dishId);
        return ResponseEntity.ok(dishHandler.enableOrDisableDish(enableDishRequestDto, id));
    }
}
