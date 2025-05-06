package com.foodquart.microservicefoodcourt.infrastructure.out.client;

import com.foodquart.microservicefoodcourt.infrastructure.configuration.FeignConfig;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.dto.CreateEmployeeRequestDto;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.dto.CreateEmployeeResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "microserviceuser",
        url = "${microservice.user.url}",
        configuration = FeignConfig.class
)
public interface IUserFeignClient {

    @PostMapping("/api/v1/user/createEmployee")
    CreateEmployeeResponseDto createEmployee(@RequestBody CreateEmployeeRequestDto createEmployeeRequestDto);


}
