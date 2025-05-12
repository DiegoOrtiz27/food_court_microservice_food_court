package com.foodquart.microservicefoodcourt.infrastructure.input.rest;

import com.foodquart.microservicefoodcourt.application.dto.request.OrderDeliveryRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.request.OrderRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.response.OrderListResponseDto;
import com.foodquart.microservicefoodcourt.application.dto.response.OrderResponseDto;
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

    @Operation(summary = "Notify order ready")
    @ApiResponse(responseCode = "204", description = "Order notified successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "403", description = "Not authorized")
    @PatchMapping("/notifyOrderReady/{id}")
    public ResponseEntity<OrderResponseDto> notifyOrderReady(
            @PathVariable("id") Long orderId) {
        Long employeeId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(orderHandler.notifyOrderReady(orderId, employeeId));
    }

    @Operation(summary = "Mark order as delivered")
    @ApiResponse(responseCode = "200", description = "Order marked as delivered successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data or invalid order status")
    @ApiResponse(responseCode = "403", description = "Not authorized or invalid security pin")
    @PatchMapping("/deliverOrder/{id}")
    public ResponseEntity<OrderResponseDto> markOrderAsDelivered(
            @PathVariable("id") Long orderId,
            @Valid @RequestBody OrderDeliveryRequestDto deliveryRequest) {
        Long employeeId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(orderHandler.markOrderAsDelivered(orderId, employeeId, deliveryRequest));
    }

    @Operation(summary = "Cancel order by customer")
    @ApiResponse(responseCode = "200", description = "Order cancelled successfully")
    @ApiResponse(responseCode = "400", description = "Invalid order status for cancellation")
    @ApiResponse(responseCode = "403", description = "Not authorized")
    @ApiResponse(responseCode = "404", description = "Order not found")
    @PatchMapping("/cancel/{id}")
    public ResponseEntity<OrderResponseDto> cancelOrder(
            @PathVariable("id") Long orderId) {
        Long customerId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(orderHandler.cancelOrder(orderId, customerId));
    }
}
