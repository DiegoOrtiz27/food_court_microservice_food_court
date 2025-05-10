package com.foodquart.microservicefoodcourt.infrastructure.out.client.mapper;

import com.foodquart.microservicefoodcourt.domain.model.CustomerModel;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.dto.response.GetUserInfoResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IUserResponseMapper {

    @Mapping(target = "customerId", source = "userId")
    CustomerModel toCustomerModel(GetUserInfoResponseDto getUserInfoResponseDto);
}
