package com.foodquart.microservicefoodcourt.domain.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenericMethodsTest {

    @Test
    void constructorShouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class, GenericMethods::new, "Utility class");
    }

}