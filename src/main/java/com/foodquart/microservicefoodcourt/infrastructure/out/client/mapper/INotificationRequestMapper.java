package com.foodquart.microservicefoodcourt.infrastructure.out.client.mapper;

import com.foodquart.microservicefoodcourt.domain.model.NotificationModel;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.dto.request.NotificationRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface INotificationRequestMapper {

    NotificationRequestDto toMapper(NotificationModel notificationModel);

}
