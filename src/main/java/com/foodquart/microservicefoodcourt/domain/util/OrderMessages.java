package com.foodquart.microservicefoodcourt.domain.util;

public class OrderMessages {
    public static final String CREATED_ORDER = "The order has been created successfully";
    public static final String ASSIGNED_ORDER = "The order has been assigned successfully";
    public static final String ORDER_READY = "The order is ready and the user has been notified";
    public static final String ORDER_DELIVERED = "Order marked as delivered successfully";
    public static final String ORDER_CANCELLED = "Order cancelled successfully";
    public static final String ORDER_ALREADY_ASSIGNED = "The order is already assigned";
    public static final String ORDER_IS_NOT_PREPARING = "The order is not preparing";
    public static final String ORDER_IS_NOT_PENDING = "The order is not pending";
    public static final String ORDER_NOT_FOUND = "Order with id '%d' not found";
    public static final String ORDER_NOT_BELONG_TO_CUSTOMER = "The order does not belong to the customer";
    public static final String EMPLOYEE_WITH_NOT_ORDER_ASSOCIATED = "The employee with id '%d' does not have the order with id '%d' associated";
    public static final String CUSTOMER_HAS_ACTIVE_ORDER = "Customer has an active order in progress";
    public static final String NOTIFICATION_NOT_SENT = "Order notification could not be sent";
    public static final String ORDER_NOT_READY_FOR_DELIVERY = "Only orders in 'READY' status can be marked as delivered";

    public static final String ORDER_ID_REQUIRED = "Order ID is required";
    public static final String EMPLOYEE_ID_REQUIRED = "Employee ID is required";
    public static final String SECURITY_PIN_REQUIRED = "Security PIN is required";
    public static final String INVALID_SECURITY_PIN = "Invalid security pin";
    public static final String CUSTOMER_ID_REQUIRED = "Customer ID is required";
    public static final String RESTAURANT_ID_REQUIRED = "Restaurant ID is required";
    public static final String ORDER_ITEMS_REQUIRED = "Order must have at least one item";

    private OrderMessages() {
        throw new IllegalStateException("Utility class");
    }
}
