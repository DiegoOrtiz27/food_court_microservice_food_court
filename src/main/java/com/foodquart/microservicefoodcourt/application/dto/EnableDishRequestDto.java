package com.foodquart.microservicefoodcourt.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnableDishRequestDto {

    private Long id;

    @NotNull(message = "Active status is required")
    private Boolean active;
}
