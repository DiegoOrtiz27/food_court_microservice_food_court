package com.foodquart.microservicefoodcourt.infrastructure.out.client.adapter;

import com.foodquart.microservicefoodcourt.domain.exception.NoDataFoundException;
import com.foodquart.microservicefoodcourt.domain.exception.UnauthorizedException;
import com.foodquart.microservicefoodcourt.domain.model.RestaurantEmployeeModel;
import com.foodquart.microservicefoodcourt.domain.spi.IUserClientPort;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.IUserFeignClient;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.dto.CreateEmployeeResponseDto;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.mapper.IEmployeeRequestMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserClientAdapter implements IUserClientPort {

    private final IUserFeignClient userFeignClient;
    private final IEmployeeRequestMapper employeeRequestMapper;

    @Override
    public RestaurantEmployeeModel createEmployee(RestaurantEmployeeModel restaurantEmployeeModel) {

        try {
            CreateEmployeeResponseDto response = userFeignClient.createEmployee(
                    employeeRequestMapper.toMapper(restaurantEmployeeModel)
            );
            restaurantEmployeeModel.setEmployeeId(response.getUserId());
            return restaurantEmployeeModel;

        } catch (FeignException.Unauthorized ex) {
            throw new UnauthorizedException("Unauthorized when trying to create employee");

        } catch (FeignException.NotFound ex) {
            throw new NoDataFoundException("The requested resource was not found");

        }
    }
}
