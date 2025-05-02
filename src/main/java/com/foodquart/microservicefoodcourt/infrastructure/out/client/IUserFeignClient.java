package com.foodquart.microservicefoodcourt.infrastructure.out.client;

import com.foodquart.microservicefoodcourt.infrastructure.configuration.FeignConfig;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.dto.GetUserByEmailResponseDto;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.dto.HasRoleResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "microserviceuser",
        url = "${microservice.user.url}",
        configuration = FeignConfig.class
)
public interface IUserFeignClient {

    @GetMapping("/api/v1/roles/{userId}/has/{roleName}")
    HasRoleResponseDto hasRole(@PathVariable("userId") Long userId, @PathVariable("roleName") String roleName);

    @GetMapping("/api/v1/user/email")
    GetUserByEmailResponseDto getUserByEmail();
}
