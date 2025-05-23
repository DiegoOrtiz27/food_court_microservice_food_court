package com.foodquart.microservicefoodcourt.domain.util;

import java.util.Random;

public class GenericMethods {
    public static final Random RANDOM = new Random();

    public static String generateSecurityPin() {
        return String.format("%04d", RANDOM.nextInt(10000));
    }

    GenericMethods() {
        throw new IllegalStateException("Utility class");
    }
}
