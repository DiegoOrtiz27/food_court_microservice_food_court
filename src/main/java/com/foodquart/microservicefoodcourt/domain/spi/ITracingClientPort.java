package com.foodquart.microservicefoodcourt.domain.spi;

import com.foodquart.microservicefoodcourt.domain.model.OrderTraceModel;

public interface ITracingClientPort {
    void recordOrderStatusChange(OrderTraceModel orderTrace);
}
