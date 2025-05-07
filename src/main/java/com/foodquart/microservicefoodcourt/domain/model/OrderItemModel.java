package com.foodquart.microservicefoodcourt.domain.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemModel {
    private Long dishId;
    private int quantity;
}
