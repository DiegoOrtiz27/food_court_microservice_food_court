package com.foodquart.microservicefoodcourt.infrastructure.out.client.adapter;

import com.foodquart.microservicefoodcourt.domain.exception.NoDataFoundException;
import com.foodquart.microservicefoodcourt.domain.exception.UnauthorizedException;
import com.foodquart.microservicefoodcourt.domain.spi.IUserClientPort;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.IUserFeignClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserClientAdapter implements IUserClientPort {

    private final IUserFeignClient userFeignClient;

    @Override
    public boolean findOwnerById(Long id) {
        try {
            return userFeignClient.hasRole(id, "OWNER").getHasRole();
        } catch (FeignException.NotFound e) {
            throw new NoDataFoundException("The owner was not found");
        } catch (FeignException.Unauthorized e) {
            throw new UnauthorizedException("Missing or invalid Authorization header");
        } catch (FeignException e) {
            throw new RuntimeException("Error communicating with the user microservice", e);
        }
    }

    @Override
    public Long getUserId() {
        try {
            return userFeignClient.getUserByEmail().getId();
        } catch (FeignException.NotFound e) {
            throw new NoDataFoundException("The user was not found");
        } catch (FeignException.Unauthorized e) {
            throw new UnauthorizedException("Missing or invalid Authorization header");
        } catch (FeignException e) {
            throw new RuntimeException("Error communicating with the user microservice", e);
        }
    }
}
