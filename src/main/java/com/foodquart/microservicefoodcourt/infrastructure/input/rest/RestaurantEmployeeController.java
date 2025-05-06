package com.foodquart.microservicefoodcourt.infrastructure.input.rest;

import com.foodquart.microservicefoodcourt.application.dto.CreateRestaurantEmployeeRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.CreateRestaurantEmployeeResponseDto;
import com.foodquart.microservicefoodcourt.application.handler.IRestaurantEmployeeHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/restaurants/{restaurantId}/employees")
@RequiredArgsConstructor
public class RestaurantEmployeeController {

    private final IRestaurantEmployeeHandler employeeHandler;

    @Operation(summary = "Add an employee to a restaurant")
    @ApiResponse(responseCode = "201", description = "Employee created and assigned successfully")
    @PostMapping("/")
    public ResponseEntity<CreateRestaurantEmployeeResponseDto> addEmployeeToRestaurant(
            @PathVariable Long restaurantId,
            @Valid @RequestBody CreateRestaurantEmployeeRequestDto createRestaurantEmployeeRequestDto) {

        Long id = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        createRestaurantEmployeeRequestDto.setRestaurantId(restaurantId);
        return ResponseEntity.ok(employeeHandler.addEmployeeToRestaurant(id, createRestaurantEmployeeRequestDto));
    }
}
