package com.foodquart.microservicefoodcourt.domain.util;

public final class HttpMessages {
    public static final String VALIDATION_FAILED = "Validation failed for one or more fields";
    public static final String SERVICE_COMMUNICATION_ERROR = "Error communicating with another service";
    public static final String INVALID_REQUEST_BODY = "Invalid request body format";
    public static final String UNKNOWN_FIELD = "Unknown field '%s' in request. Accepted fields: %s";
    public static final String INVALID_REQUEST_FORMAT = "Invalid request format";
    public static final String INVALID_JSON = "Malformed JSON request";
    public static final String UNEXPECTED_ERROR = "An unexpected error occurred";

    private HttpMessages() {
        throw new IllegalStateException("Utility class");
    }
}
