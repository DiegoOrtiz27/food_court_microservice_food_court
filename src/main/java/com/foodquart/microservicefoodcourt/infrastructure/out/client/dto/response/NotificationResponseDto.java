package com.foodquart.microservicefoodcourt.infrastructure.out.client.dto.response;

import lombok.*;

@Getter
@Setter
public class NotificationResponseDto {
    private boolean success;
    private String response;
}
