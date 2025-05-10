package com.foodquart.microservicefoodcourt.infrastructure.out.client.adapter;

import com.foodquart.microservicefoodcourt.domain.exception.NoDataFoundException;
import com.foodquart.microservicefoodcourt.domain.exception.UnauthorizedException;
import com.foodquart.microservicefoodcourt.domain.model.CustomerModel;
import com.foodquart.microservicefoodcourt.domain.model.RestaurantEmployeeModel;
import com.foodquart.microservicefoodcourt.domain.spi.IUserClientPort;
import com.foodquart.microservicefoodcourt.domain.util.SecurityMessages;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.IUserFeignClient;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.dto.response.CreateEmployeeResponseDto;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.mapper.IUserRequestMapper;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.mapper.IUserResponseMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserClientAdapter implements IUserClientPort {

    private final IUserFeignClient userFeignClient;
    private final IUserRequestMapper userRequestMapper;
    private final IUserResponseMapper userResponseMapper;

    @Override
    public RestaurantEmployeeModel createEmployee(RestaurantEmployeeModel restaurantEmployeeModel) {
        try {
            CreateEmployeeResponseDto response = userFeignClient.createEmployee(
                    userRequestMapper.toMapper(restaurantEmployeeModel)
            );
            restaurantEmployeeModel.setEmployeeId(response.getUserId());
            return restaurantEmployeeModel;

        } catch (FeignException.Unauthorized ex) {
            throw new UnauthorizedException(String.format(SecurityMessages.UNAUTHORIZED, "create employee"));
        } catch (FeignException.NotFound ex) {
            throw new NoDataFoundException(SecurityMessages.NOT_FOUND);
        }
    }

    @Override
    public CustomerModel getUserInfo(Long userId) {
        try {
            return userResponseMapper.toCustomerModel(userFeignClient.getUserInfo(userId));
        } catch (FeignException.Unauthorized ex) {
            throw new UnauthorizedException(String.format(SecurityMessages.UNAUTHORIZED, "get user information"));
        } catch (FeignException.NotFound ex) {
            throw new NoDataFoundException(SecurityMessages.NOT_FOUND);
        }
    }
}
