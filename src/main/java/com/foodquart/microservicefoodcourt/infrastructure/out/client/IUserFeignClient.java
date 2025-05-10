package com.foodquart.microservicefoodcourt.infrastructure.out.client;

import com.foodquart.microservicefoodcourt.infrastructure.configuration.FeignConfig;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.dto.request.CreateEmployeeRequestDto;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.dto.response.CreateEmployeeResponseDto;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.dto.response.GetUserInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/api/v1/user/{userId}")
    GetUserInfoResponseDto getUserInfo(@PathVariable Long userId);

}
