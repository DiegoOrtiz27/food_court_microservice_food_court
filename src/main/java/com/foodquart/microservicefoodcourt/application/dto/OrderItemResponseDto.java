package com.foodquart.microservicefoodcourt.application.dto;

import lombok.*;

@Getter
@Setter
public class OrderItemResponseDto {
    private String dishName;
    private Integer quantity;
    private int price;
}
