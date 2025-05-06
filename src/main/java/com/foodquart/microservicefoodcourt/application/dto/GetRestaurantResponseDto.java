package com.foodquart.microservicefoodcourt.application.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetRestaurantResponseDto {
    private String name;
    private String logoUrl;
}
