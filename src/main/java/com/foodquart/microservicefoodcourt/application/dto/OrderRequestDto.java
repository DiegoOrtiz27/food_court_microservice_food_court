package com.foodquart.microservicefoodcourt.application.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class OrderRequestDto {
    @NotNull(message = "Restaurant ID is required")
    @Positive(message = "Restaurant ID must be positive")
    private Long restaurantId;

    @NotEmpty(message = "Items list cannot be empty")
    private List<OrderItemRequestDto> items;
}
