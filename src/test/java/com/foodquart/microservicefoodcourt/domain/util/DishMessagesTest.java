package com.foodquart.microservicefoodcourt.domain.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DishMessagesTest {
    @Test
    void constructorShouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class, DishMessages::new, "Utility class");
    }
}