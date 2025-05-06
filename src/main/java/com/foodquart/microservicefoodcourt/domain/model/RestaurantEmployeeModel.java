package com.foodquart.microservicefoodcourt.domain.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantEmployeeModel {
    private Long employeeId;
    private String firstName;
    private String lastName;
    private String documentId;
    private String phone;
    private String email;
    private String password;
    private Long restaurantId;
}
