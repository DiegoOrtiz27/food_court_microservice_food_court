package com.foodquart.microservicefoodcourt.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class RestaurantRequestDto {
    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^(?!\\d+$)[\\w ]+$", message = "Name can contain numbers but not be only numbers")
    private String name;

    @NotBlank(message = "NIT is required")
    @Pattern(regexp = "\\d+", message = "NIT must contain only numbers")
    private String nit;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Phone is required")
    @Size(max = 13, message = "Phone cannot exceed 13 characters")
    @Pattern(regexp = "^\\+?\\d+$", message = "Phone must be numeric and can start with +")
    private String phone;

    @NotBlank(message = "Logo URL is required")
    private String logoUrl;

    @NotNull(message = "Owner ID is required")
    private Long ownerId;
}