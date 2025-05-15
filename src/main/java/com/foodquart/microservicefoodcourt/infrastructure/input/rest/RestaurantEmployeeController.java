package com.foodquart.microservicefoodcourt.infrastructure.input.rest;

import com.foodquart.microservicefoodcourt.application.dto.request.CreateRestaurantEmployeeRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.response.CreateRestaurantEmployeeResponseDto;
import com.foodquart.microservicefoodcourt.application.handler.IRestaurantEmployeeHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.foodquart.microservicefoodcourt.infrastructure.documentation.APIRestaurantEmployeeDocumentationConstant.*;
import static com.foodquart.microservicefoodcourt.infrastructure.documentation.ResponseCode.*;

@RestController
@RequestMapping("/api/v1/restaurants/{restaurantId}/employees")
@RequiredArgsConstructor
public class RestaurantEmployeeController {

    private final IRestaurantEmployeeHandler employeeHandler;

    @PostMapping("/")
    @Operation(summary = ADD_EMPLOYEE_TO_RESTAURANT_SUMMARY)
    @ApiResponse(responseCode = CODE_201, description = ADD_EMPLOYEE_TO_RESTAURANT_SUCCESS_DESCRIPTION)
    public ResponseEntity<CreateRestaurantEmployeeResponseDto> addEmployeeToRestaurant(
            @PathVariable Long restaurantId,
            @Valid @RequestBody CreateRestaurantEmployeeRequestDto createRestaurantEmployeeRequestDto) {
        return ResponseEntity.ok(employeeHandler.addEmployeeToRestaurant(restaurantId, createRestaurantEmployeeRequestDto));
    }
}
