package com.foodquart.microservicefoodcourt.infrastructure.out.client.adapter;

import com.foodquart.microservicefoodcourt.domain.spi.IUserClientPort;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.IUserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserClientAdapter implements IUserClientPort {

    private final IUserFeignClient userFeignClient;

    @Override
    public boolean findOwnerById(Long id) {
        return userFeignClient.isOwner(id).isOwner();
    }
}
