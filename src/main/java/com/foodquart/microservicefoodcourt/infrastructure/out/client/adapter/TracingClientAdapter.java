package com.foodquart.microservicefoodcourt.infrastructure.out.client.adapter;

import com.foodquart.microservicefoodcourt.domain.exception.NoDataFoundException;
import com.foodquart.microservicefoodcourt.domain.exception.UnauthorizedException;
import com.foodquart.microservicefoodcourt.domain.model.OrderTraceModel;
import com.foodquart.microservicefoodcourt.domain.spi.ITracingClientPort;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.ITracingFeignClient;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.mapper.IOrderTraceRequestMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.foodquart.microservicefoodcourt.domain.util.SecurityMessages.NOT_FOUND;
import static com.foodquart.microservicefoodcourt.domain.util.SecurityMessages.UNAUTHORIZED;

@Component
@RequiredArgsConstructor
public class TracingClientAdapter implements ITracingClientPort {

    private final ITracingFeignClient tracingFeignClient;
    private final IOrderTraceRequestMapper orderTraceRequestMapper;

    @Override
    public void recordOrderStatusChange(OrderTraceModel orderTrace) {
        try {
            tracingFeignClient.recordTrace(orderTraceRequestMapper.toMapper(orderTrace));
        } catch (FeignException.Unauthorized ex) {
            throw new UnauthorizedException(String.format(UNAUTHORIZED, "record trace"));
        } catch (FeignException.NotFound ex) {
            throw new NoDataFoundException(NOT_FOUND);
        }
    }
}
