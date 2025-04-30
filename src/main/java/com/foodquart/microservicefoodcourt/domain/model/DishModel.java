package com.foodquart.microservicefoodcourt.domain.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DishModel {
    private Long id;
    private String name;
    private Integer price;
    private String description;
    private String imageUrl;
    private String category;
    private Boolean active;
    private Long restaurantId;
}
