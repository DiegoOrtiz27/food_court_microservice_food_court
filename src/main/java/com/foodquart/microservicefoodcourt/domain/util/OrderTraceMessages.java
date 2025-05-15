package com.foodquart.microservicefoodcourt.domain.util;

public class OrderTraceMessages {

    public static final String ORDER_CREATED = "Order created with %d items";
    public static final String ORDER_ASSIGNED = "Order assigned to employee: %d";
    public static final String ORDER_DELIVERED = "Order delivered by employee: %d";
    public static final String ORDER_CANCELLED = "Order cancelled by customer: %d";

    OrderTraceMessages() {
        throw new IllegalStateException("Utility class");
    }
}
