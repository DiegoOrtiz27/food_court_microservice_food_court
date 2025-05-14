package com.foodquart.microservicefoodcourt.infrastructure.out.client.dto.request;

import lombok.*;

@Getter
@Setter
public class NotificationRequestDto {
    private String phoneNumber;
    private String message;
}
