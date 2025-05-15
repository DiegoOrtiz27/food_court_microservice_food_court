package com.foodquart.microservicefoodcourt.domain.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderTraceMessagesTest {

    @Test
    void constructorShouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class, OrderTraceMessages::new, "Utility class");
    }

}