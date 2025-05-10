package com.foodquart.microservicefoodcourt.infrastructure.out.client;


import com.foodquart.microservicefoodcourt.infrastructure.configuration.FeignConfig;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.dto.request.NotificationRequestDto;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.dto.response.NotificationResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "microservicemessaging",
        url = "${microservice.messaging.url}",
        configuration = FeignConfig.class
)
public interface IMessagingFeignClient {
    @PostMapping("/api/v1/notifications/sms")
    NotificationResponseDto notifyOrderReady(@RequestBody NotificationRequestDto notificationRequestDto);
}
