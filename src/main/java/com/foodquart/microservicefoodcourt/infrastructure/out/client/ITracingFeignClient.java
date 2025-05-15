package com.foodquart.microservicefoodcourt.infrastructure.out.client;

import com.foodquart.microservicefoodcourt.infrastructure.configuration.FeignConfig;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.dto.request.OrderTraceRequestDto;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.dto.response.OrderTraceResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "microservicetracing",
        url = "${microservice.tracing.url}",
        configuration = FeignConfig.class
)
public interface ITracingFeignClient {

    @PostMapping("/api/v1/tracing/")
    OrderTraceResponseDto recordTrace(@RequestBody OrderTraceRequestDto orderTraceRequestDto);
}
