package com.foodquart.microservicefoodcourt.infrastructure.out.client.adapter;

import com.foodquart.microservicefoodcourt.domain.exception.NoDataFoundException;
import com.foodquart.microservicefoodcourt.domain.exception.UnauthorizedException;
import com.foodquart.microservicefoodcourt.domain.model.NotificationModel;
import com.foodquart.microservicefoodcourt.domain.spi.IMessagingClientPort;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.IMessagingFeignClient;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.dto.response.NotificationResponseDto;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.mapper.INotificationRequestMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.foodquart.microservicefoodcourt.domain.util.SecurityMessages.NOT_FOUND;
import static com.foodquart.microservicefoodcourt.domain.util.SecurityMessages.UNAUTHORIZED;

@Component
@RequiredArgsConstructor
public class MessagingClientAdapter implements IMessagingClientPort {

    private final IMessagingFeignClient messagingFeignClient;
    private final INotificationRequestMapper notificationRequestMapper;

    @Override
    public boolean notifyOrderReady(NotificationModel notificationModel) {
        try {
            NotificationResponseDto notificationResponseDto = messagingFeignClient.notifyOrderReady(notificationRequestMapper.toMapper(notificationModel));
            return notificationResponseDto.isSuccess();

        } catch (FeignException.Unauthorized ex) {
            throw new UnauthorizedException(String.format(UNAUTHORIZED, "sent message"));

        } catch (FeignException.NotFound ex) {
            throw new NoDataFoundException(NOT_FOUND);

        }
    }
}
