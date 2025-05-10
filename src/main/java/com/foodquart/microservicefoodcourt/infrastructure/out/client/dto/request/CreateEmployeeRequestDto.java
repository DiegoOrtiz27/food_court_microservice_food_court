package com.foodquart.microservicefoodcourt.infrastructure.out.client.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEmployeeRequestDto {
    private String firstName;
    private String lastName;
    private String documentId;
    private String phone;
    private String email;
    private String password;
}
