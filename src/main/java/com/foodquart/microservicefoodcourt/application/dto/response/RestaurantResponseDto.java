package com.foodquart.microservicefoodcourt.application.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantResponseDto {
    private Long restaurantId;
    private String response;
}
