package com.foodquart.microservicefoodcourt.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDeliveryRequestDto {
    @NotBlank(message = "Security pin is required")
    @Pattern(regexp = "\\d{4}", message = "Security pin must be 4 digits")
    private String securityPin;
}