package com.foodquart.microservicefoodcourt.infrastructure.input.rest;

import com.foodquart.microservicefoodcourt.application.dto.UpdateDishRequestDto;
import com.foodquart.microservicefoodcourt.application.handler.IDishHandler;
import com.foodquart.microservicefoodcourt.application.dto.DishRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Void> createDish(
            @Valid @RequestBody DishRequestDto dishRequestDto) {

        dishHandler.createDish(dishRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Update a dish (price and description only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Dish updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "403", description = "Not authorized")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateDish(
            @PathVariable("id") Long dishId,
            @Valid @RequestBody UpdateDishRequestDto updateDishRequestDto) {

        updateDishRequestDto.setId(dishId);
        dishHandler.updateDish(updateDishRequestDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
