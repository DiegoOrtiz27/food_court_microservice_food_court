package com.foodquart.microservicefoodcourt.domain.spi;

public interface IJwtProviderPort {

    boolean validateToken(String token);
    Long getIdFromToken(String token);
    String getRoleFromToken(String token);
}
