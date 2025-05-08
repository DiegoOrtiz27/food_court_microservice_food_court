package com.foodquart.microservicefoodcourt.domain.util;

public class OrderMessages {
    public static final String CREATED_ORDER = "The order has been created successfully";
    public static final String ASSIGNED_ORDER = "The order has been assigned successfully";
    public static final String ORDER_ALREADY_ASSIGNED = "The order is already assigned";
    public static final String CUSTOMER_ID_REQUIRED = "Customer ID is required";
    public static final String RESTAURANT_ID_REQUIRED = "Restaurant ID is required";
    public static final String ORDER_ITEMS_REQUIRED = "Order must have at least one item";
    public static final String CUSTOMER_HAS_ACTIVE_ORDER = "Customer has an active order in progress";
    public static final String ORDER_NOT_FOUND = "Order with id '%d' not found";
    public static final String INVALID_STATUS_TRANSITION = "Invalid status transition from %s to %s";
    public static final String ORDER_ALREADY_CANCELLED = "Order is already cancelled";
    public static final String ORDER_NOT_PENDING = "Order cannot be cancelled because it's not in PENDING status";
    public static final String INVALID_SECURITY_PIN = "Invalid security pin";

    private OrderMessages() {
        throw new IllegalStateException("Utility class");
    }
}
