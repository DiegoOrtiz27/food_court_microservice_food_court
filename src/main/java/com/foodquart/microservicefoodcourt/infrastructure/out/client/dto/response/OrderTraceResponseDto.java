package com.foodquart.microservicefoodcourt.infrastructure.out.client.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderTraceResponseDto {
    private String id;
    private Long orderId;
    private Long customerId;
    private Long restaurantId;
    private Long employeeId;
    private String previousStatus;
    private String newStatus;
    private LocalDateTime timestamp;
    private String notes;
}
