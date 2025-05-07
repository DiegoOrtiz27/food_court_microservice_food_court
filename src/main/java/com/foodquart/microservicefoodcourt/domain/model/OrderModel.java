package com.foodquart.microservicefoodcourt.domain.model;

import com.foodquart.microservicefoodcourt.domain.util.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderModel {
    private Long id;
    private Long customerId;
    private Long restaurantId;
    private List<OrderItemModel> items;
    private OrderStatus status;
    private Long assignedEmployeeId;
    private String securityPin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
