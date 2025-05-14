package com.foodquart.microservicefoodcourt.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class CreateRestaurantEmployeeRequestDto {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Document ID is required")
    @Pattern(regexp = "\\d+", message = "Document ID must be numeric")
    private String documentId;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\+?\\d{1,13}", message = "Phone number is invalid (up to 13 digits, optional +)")
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
}
