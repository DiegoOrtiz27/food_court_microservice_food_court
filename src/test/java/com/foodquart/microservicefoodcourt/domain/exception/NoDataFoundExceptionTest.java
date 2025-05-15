package com.foodquart.microservicefoodcourt.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoDataFoundExceptionTest {

    @Test
    void shouldCreateNoDataFoundExceptionWithMessage() {
        String errorMessage = "Resource not found.";
        NoDataFoundException exception = new NoDataFoundException(errorMessage);
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }
}