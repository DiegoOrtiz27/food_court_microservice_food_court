package com.foodquart.microservicefoodcourt.domain.spi;

import com.foodquart.microservicefoodcourt.domain.model.NotificationModel;

public interface IMessagingClientPort {
    boolean notifyOrderReady(NotificationModel notificationModel);
}
