package com.foodquart.microservicefoodcourt.infrastructure.input.rest;

import com.foodquart.microservicefoodcourt.application.handler.IDishHandler;
import com.foodquart.microservicefoodcourt.application.request.DishRequestDto;
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
            @Valid @RequestBody DishRequestDto requestDto,
            @RequestHeader("X-Owner-Id") Long ownerId) {

        dishHandler.createDish(requestDto, ownerId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
