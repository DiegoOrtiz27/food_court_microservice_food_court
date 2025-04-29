package com.foodquart.microservicefoodcourt.infrastructure.out.client;

import com.foodquart.microservicefoodcourt.infrastructure.out.client.dto.IsOwnerResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microserviceuser", url = "${microservice.user.url}")
public interface IUserFeignClient {
    @GetMapping("/api/v1/owner/{ownerId}/is-owner")
    IsOwnerResponseDto isOwner(@PathVariable("ownerId") Long ownerId);
}
