package com.foodquart.microservicefoodcourt.application.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantListResponseDto {
    private String name;
    private String logoUrl;
}
