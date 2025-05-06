package com.foodquart.microservicefoodcourt.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRestaurantEmployeeResponseDto {
    private String response;
    private Long employeeId;
}
