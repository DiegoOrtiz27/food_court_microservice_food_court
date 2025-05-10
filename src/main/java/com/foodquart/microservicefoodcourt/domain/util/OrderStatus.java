package com.foodquart.microservicefoodcourt.domain.util;

import java.util.Arrays;
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

    public boolean isNotIn(OrderStatus... forbiddenStatuses) {
        return !Arrays.asList(forbiddenStatuses).contains(this);
    }

    public boolean isNotAssignedStatus() {
        return isNotIn(IN_PREPARATION, READY, DELIVERED, CANCELLED);
    }
}
