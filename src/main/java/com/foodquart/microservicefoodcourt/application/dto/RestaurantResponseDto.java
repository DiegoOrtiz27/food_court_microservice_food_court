package com.foodquart.microservicefoodcourt.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantResponseDto {
    private Long restaurantId;
    private String response;
}
