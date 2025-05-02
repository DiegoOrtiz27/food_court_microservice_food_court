package com.foodquart.microservicefoodcourt.infrastructure.out.client.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUserByEmailResponseDto {
    private Long id;
    private String email;
}
