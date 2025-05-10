package com.foodquart.microservicefoodcourt.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class UpdateDishRequestDto {

    private Long id;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    @Min(value = 1, message = "Price must be greater than 0")
    private Integer price;
}