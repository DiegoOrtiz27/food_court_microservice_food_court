package com.foodquart.microservicefoodcourt.application.dto.response;

import lombok.*;

@Getter
@AllArgsConstructor
public class RestaurantItemResponse {
    private String name;
    private String logoUrl;
}
