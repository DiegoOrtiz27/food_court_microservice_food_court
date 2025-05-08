package com.foodquart.microservicefoodcourt.domain.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemModel {
    private Long id;
    private int quantity;
    private DishModel dish;
}
