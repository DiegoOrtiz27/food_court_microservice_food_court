package com.foodquart.microservicefoodcourt.domain.model;

import com.foodquart.microservicefoodcourt.domain.util.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderTraceModel {
    private String id;
    private Long orderId;
    private Long customerId;
    private Long restaurantId;
    private Long employeeId;
    private OrderStatus previousStatus;
    private OrderStatus newStatus;
    private LocalDateTime timestamp;
    private String notes;
}
