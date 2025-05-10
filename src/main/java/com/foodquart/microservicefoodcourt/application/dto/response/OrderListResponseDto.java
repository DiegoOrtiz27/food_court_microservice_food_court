package com.foodquart.microservicefoodcourt.application.dto.response;

import com.foodquart.microservicefoodcourt.domain.util.OrderStatus;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderListResponseDto {
    private Long id;
    private Long customerId;
    private String customerName;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItemResponseDto> items;
}
