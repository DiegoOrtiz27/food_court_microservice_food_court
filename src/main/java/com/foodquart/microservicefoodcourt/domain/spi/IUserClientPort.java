package com.foodquart.microservicefoodcourt.domain.spi;

public interface IUserClientPort {
    boolean findOwnerById(Long id);
    Long getUserId();
}
