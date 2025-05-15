package com.foodquart.microservicefoodcourt.infrastructure.documentation;

public class APIOrderDocumentationConstant {

    public static final String CREATE_ORDER_SUMMARY = "Create a new order";
    public static final String CREATE_ORDER_SUCCESS_DESCRIPTION = "Order created successfully";
    public static final String CREATE_ORDER_INVALID_INPUT_DESCRIPTION = "Invalid input data";
    public static final String CREATE_ORDER_CONFLICT_DESCRIPTION = "Customer already has an active order";

    public static final String GET_ORDERS_BY_RESTAURANT_SUMMARY = "Get orders by restaurant with pagination and status filter";
    public static final String GET_ORDERS_BY_RESTAURANT_SUCCESS_DESCRIPTION = "Order retrieved successfully";
    public static final String GET_ORDERS_BY_RESTAURANT_NOT_FOUND_DESCRIPTION = "Restaurant not found";

    public static final String ASSIGN_ORDER_SUMMARY = "Assign order to employee";
    public static final String ASSIGN_ORDER_SUCCESS_DESCRIPTION = "Order assigned successfully";
    public static final String ASSIGN_ORDER_INVALID_INPUT_DESCRIPTION = "Invalid input data";
    public static final String ASSIGN_ORDER_UNAUTHORIZED_DESCRIPTION = "Not authorized";

    public static final String NOTIFY_ORDER_READY_SUMMARY = "Notify order ready";
    public static final String NOTIFY_ORDER_READY_SUCCESS_DESCRIPTION = "Order notified successfully";
    public static final String NOTIFY_ORDER_READY_INVALID_INPUT_DESCRIPTION = "Invalid input data";
    public static final String NOTIFY_ORDER_READY_UNAUTHORIZED_DESCRIPTION = "Not authorized";

    public static final String MARK_ORDER_DELIVERED_SUMMARY = "Mark order as delivered";
    public static final String MARK_ORDER_DELIVERED_SUCCESS_DESCRIPTION = "Order marked as delivered successfully";
    public static final String MARK_ORDER_DELIVERED_INVALID_INPUT_DESCRIPTION = "Invalid input data or invalid order status";
    public static final String MARK_ORDER_DELIVERED_UNAUTHORIZED_DESCRIPTION = "Not authorized or invalid security pin";

    public static final String CANCEL_ORDER_SUMMARY = "Cancel order by customer";
    public static final String CANCEL_ORDER_SUCCESS_DESCRIPTION = "Order cancelled successfully";
    public static final String CANCEL_ORDER_INVALID_STATUS_DESCRIPTION = "Invalid order status for cancellation";
    public static final String CANCEL_ORDER_UNAUTHORIZED_DESCRIPTION = "Not authorized";
    public static final String CANCEL_ORDER_NOT_FOUND_DESCRIPTION = "Order not found";

    private APIOrderDocumentationConstant() {
    }
}
