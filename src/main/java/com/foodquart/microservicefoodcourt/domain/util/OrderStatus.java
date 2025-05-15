package com.foodquart.microservicefoodcourt.domain.util;

import java.util.List;

public enum OrderStatus {
    PENDING,
    IN_PREPARATION,
    READY,
    DELIVERED,
    CANCELLED;

    public static List<OrderStatus> getActiveStatuses() {
        return List.of(PENDING, IN_PREPARATION, READY);
    }
}
