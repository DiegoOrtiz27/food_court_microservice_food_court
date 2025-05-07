package com.foodquart.microservicefoodcourt.application.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class OrderItemRequestDto {
    @NotNull(message = "Dish ID is required")
    @Positive(message = "Dish ID must be positive")
    private Long dishId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}
