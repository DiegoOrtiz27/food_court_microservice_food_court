package com.foodquart.microservicefoodcourt.domain.util;

import java.util.Set;

public class SecurityMessages {

    public static final String MISSING_AUTH_HEADER = "Missing or invalid Authorization header";
    public static final String ACCESS_DENIED = "You do not have permission to access this resource";
    public static final String INVALID_EXPIRED_TOKEN = "Invalid or expired token";
    public static final String NOT_FOUND = "The requested resource was not found";
    public static final String UNAUTHORIZED = "Unauthorized when trying to '%s'";


    public static final Set<String> PUBLIC_PATHS = Set.of(
            "/swagger-ui.html",
            "/swagger-ui/index.html",
            "/swagger-ui/",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/v3/api-docs/**"
    );

    SecurityMessages() {
        throw new IllegalStateException("Utility class");
    }
}
