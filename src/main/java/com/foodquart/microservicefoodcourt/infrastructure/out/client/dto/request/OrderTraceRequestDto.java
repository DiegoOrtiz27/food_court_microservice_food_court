package com.foodquart.microservicefoodcourt.infrastructure.out.client.dto.request;

import lombok.*;

@Getter
@Setter
public class OrderTraceRequestDto {
    private Long orderId;
    private Long customerId;
    private Long restaurantId;
    private Long employeeId;
    private String previousStatus;
    private String newStatus;
    private String notes;
}
