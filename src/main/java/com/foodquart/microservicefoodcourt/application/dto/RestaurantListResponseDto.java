package com.foodquart.microservicefoodcourt.application.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantListResponseDto {
    private String name;
    private String logoUrl;
}
