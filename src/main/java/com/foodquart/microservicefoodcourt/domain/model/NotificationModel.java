package com.foodquart.microservicefoodcourt.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationModel {
    private String phoneNumber;
    private String securityPin;
    private Long orderId;
}
