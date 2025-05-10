package com.foodquart.microservicefoodcourt.application.dto.response;

import lombok.*;

@Getter
@Setter
public class OrderResponseDto {
    private Long orderId;
    private String status;
    private String response;
}
