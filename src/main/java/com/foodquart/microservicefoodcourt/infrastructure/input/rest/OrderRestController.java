package com.foodquart.microservicefoodcourt.infrastructure.input.rest;

import com.foodquart.microservicefoodcourt.application.dto.*;
import com.foodquart.microservicefoodcourt.application.handler.IOrderHandler;
import com.foodquart.microservicefoodcourt.domain.util.OrderStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderRestController {
    private final IOrderHandler orderHandler;

    @Operation(summary = "Create a new order")
    @ApiResponse(responseCode = "201", description = "Order created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "409", description = "Customer already has an active order")
    @PostMapping("/")
    public ResponseEntity<OrderResponseDto> createOrder(
            @Valid @RequestBody OrderRequestDto orderRequestDto) {
        Long customerId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(orderHandler.createOrder(orderRequestDto, customerId));
    }

    @Operation(summary = "Get orders by restaurant with pagination and status filter")
    @ApiResponse(responseCode = "200", description = "Order retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Restaurant not found")
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<Page<OrderListResponseDto>> getDishesByRestaurant(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Long employeeId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(orderHandler.getOrdersByRestaurant(employeeId, restaurantId, status, page, size));
    }

    @Operation(summary = "Assign order to employee")
    @ApiResponse(responseCode = "204", description = "Order assigned successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "403", description = "Not authorized")
    @PatchMapping("/{id}")
    public ResponseEntity<OrderResponseDto> assignOrderToEmployee(
            @PathVariable("id") Long orderId) {
        Long employeeId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(orderHandler.assignOrderToEmployee(orderId, employeeId));
    }
}
