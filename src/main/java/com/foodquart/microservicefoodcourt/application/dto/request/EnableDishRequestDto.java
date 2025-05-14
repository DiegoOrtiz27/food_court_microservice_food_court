package com.foodquart.microservicefoodcourt.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnableDishRequestDto {
    @NotNull(message = "Active status is required")
    private Boolean active;
}
