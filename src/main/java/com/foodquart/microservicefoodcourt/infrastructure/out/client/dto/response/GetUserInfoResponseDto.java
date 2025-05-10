package com.foodquart.microservicefoodcourt.infrastructure.out.client.dto.response;

import lombok.*;

@Getter
@Setter
public class GetUserInfoResponseDto {
    private Long userId;
    private String phone;
}
