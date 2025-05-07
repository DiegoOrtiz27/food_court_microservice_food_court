package com.foodquart.microservicefoodcourt.application.dto;

import lombok.*;

@Getter
@Setter
public class OrderResponseDto {
    private Long orderId;
    private String status;
    private String response;
}
