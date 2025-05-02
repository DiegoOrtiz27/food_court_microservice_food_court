package com.foodquart.microservicefoodcourt.domain.spi;

public interface IJwtProviderPort {

    boolean validateToken(String token);
    String getEmailFromToken(String token);
    String getRoleFromToken(String token);
}
