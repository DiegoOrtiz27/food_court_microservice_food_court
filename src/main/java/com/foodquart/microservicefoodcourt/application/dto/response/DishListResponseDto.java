package com.foodquart.microservicefoodcourt.application.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishListResponseDto {
    private Long id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private String category;
    private boolean active;
}
