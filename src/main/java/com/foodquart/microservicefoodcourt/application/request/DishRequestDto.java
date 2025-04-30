package com.foodquart.microservicefoodcourt.application.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishRequestDto {
    @NotBlank(message = "Dish name is required")
    @Pattern(regexp = "^(?!\\d+$)[\\p{L}\\d .'-]+$",
            message = "Dish name can contain numbers but not be only numbers")
    @Size(max = 100, message = "Dish name cannot exceed 100 characters")
    private String name;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be a positive number greater than 0")
    @Min(value = 1, message = "Price must be at least 1")
    private Integer price;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 500, message = "Description must be between 10-500 characters")
    private String description;

    @NotBlank(message = "Image URL is required")
    @Pattern(regexp = "^https?://\\S+$",
            message = "Image URL must be a valid HTTP/HTTPS URL")
    private String imageUrl;

    @NotBlank(message = "Category is required")
    @Size(max = 50, message = "Category cannot exceed 50 characters")
    private String category;

    @NotNull(message = "Restaurant ID is required")
    @Positive(message = "Restaurant ID must be a positive number")
    private Long restaurantId;
}
