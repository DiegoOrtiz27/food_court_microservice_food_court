package com.foodquart.microservicefoodcourt.infrastructure.input.rest;

import com.foodquart.microservicefoodcourt.application.dto.request.OrderDeliveryRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.request.OrderRequestDto;
import com.foodquart.microservicefoodcourt.application.dto.response.OrderListResponseDto;
import com.foodquart.microservicefoodcourt.application.dto.response.OrderResponseDto;
import com.foodquart.microservicefoodcourt.application.dto.response.PaginationListResponseDto;
import com.foodquart.microservicefoodcourt.application.handler.IOrderHandler;
import com.foodquart.microservicefoodcourt.domain.util.OrderStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.foodquart.microservicefoodcourt.infrastructure.documentation.APIOrderDocumentationConstant.*;
import static com.foodquart.microservicefoodcourt.infrastructure.documentation.ResponseCode.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderRestController {
    private final IOrderHandler orderHandler;

    @PostMapping("/")
    @Operation(summary = CREATE_ORDER_SUMMARY)
    @ApiResponse(responseCode = CODE_201, description = CREATE_ORDER_SUCCESS_DESCRIPTION)
    @ApiResponse(responseCode = CODE_400, description = CREATE_ORDER_INVALID_INPUT_DESCRIPTION)
    @ApiResponse(responseCode = CODE_409, description = CREATE_ORDER_CONFLICT_DESCRIPTION)
    public ResponseEntity<OrderResponseDto> createOrder(
            @Valid @RequestBody OrderRequestDto orderRequestDto) {
        return ResponseEntity.ok(orderHandler.createOrder(orderRequestDto));
    }

    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = GET_ORDERS_BY_RESTAURANT_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description = GET_ORDERS_BY_RESTAURANT_SUCCESS_DESCRIPTION)
    @ApiResponse(responseCode = CODE_404, description = GET_ORDERS_BY_RESTAURANT_NOT_FOUND_DESCRIPTION)
    public ResponseEntity<PaginationListResponseDto<OrderListResponseDto>> getOrdersByRestaurant(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PaginationListResponseDto<OrderListResponseDto> response =
                orderHandler.getOrdersByRestaurant(restaurantId, status, page, size);

        if (response.getContent().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    @Operation(summary = ASSIGN_ORDER_SUMMARY)
    @ApiResponse(responseCode = CODE_204, description = ASSIGN_ORDER_SUCCESS_DESCRIPTION)
    @ApiResponse(responseCode = CODE_400, description = ASSIGN_ORDER_INVALID_INPUT_DESCRIPTION)
    @ApiResponse(responseCode = CODE_403, description = ASSIGN_ORDER_UNAUTHORIZED_DESCRIPTION)
    public ResponseEntity<OrderResponseDto> assignOrderToEmployee(
            @PathVariable("id") Long orderId) {
        return ResponseEntity.ok(orderHandler.assignOrderToEmployee(orderId));
    }

    @PatchMapping("/notifyOrderReady/{id}")
    @Operation(summary = NOTIFY_ORDER_READY_SUMMARY)
    @ApiResponse(responseCode = CODE_204, description = NOTIFY_ORDER_READY_SUCCESS_DESCRIPTION)
    @ApiResponse(responseCode = CODE_400, description = NOTIFY_ORDER_READY_INVALID_INPUT_DESCRIPTION)
    @ApiResponse(responseCode = CODE_403, description = NOTIFY_ORDER_READY_UNAUTHORIZED_DESCRIPTION)
    public ResponseEntity<OrderResponseDto> notifyOrderReady(
            @PathVariable("id") Long orderId) {
        return ResponseEntity.ok(orderHandler.notifyOrderReady(orderId));
    }

    @PatchMapping("/deliverOrder/{id}")
    @Operation(summary = MARK_ORDER_DELIVERED_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description = MARK_ORDER_DELIVERED_SUCCESS_DESCRIPTION)
    @ApiResponse(responseCode = CODE_400, description = MARK_ORDER_DELIVERED_INVALID_INPUT_DESCRIPTION)
    @ApiResponse(responseCode = CODE_403, description = MARK_ORDER_DELIVERED_UNAUTHORIZED_DESCRIPTION)
    public ResponseEntity<OrderResponseDto> markOrderAsDelivered(
            @PathVariable("id") Long orderId,
            @Valid @RequestBody OrderDeliveryRequestDto deliveryRequest) {
        return ResponseEntity.ok(orderHandler.markOrderAsDelivered(orderId, deliveryRequest));
    }

    @PatchMapping("/cancel/{id}")
    @Operation(summary = CANCEL_ORDER_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description = CANCEL_ORDER_SUCCESS_DESCRIPTION)
    @ApiResponse(responseCode = CODE_400, description = CANCEL_ORDER_INVALID_STATUS_DESCRIPTION)
    @ApiResponse(responseCode = CODE_403, description = CANCEL_ORDER_UNAUTHORIZED_DESCRIPTION)
    @ApiResponse(responseCode = CODE_404, description = CANCEL_ORDER_NOT_FOUND_DESCRIPTION)
    public ResponseEntity<OrderResponseDto> cancelOrder(
            @PathVariable("id") Long orderId) {
        return ResponseEntity.ok(orderHandler.cancelOrder(orderId));
    }
}
