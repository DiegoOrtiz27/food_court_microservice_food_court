package com.foodquart.microservicefoodcourt.domain.model;

import com.foodquart.microservicefoodcourt.domain.util.OrderStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderModelTest {

    @Test
    void shouldCreateOrderModelWithAllArgsConstructor() {
        Long id = 1L;
        Long customerId = 101L;
        Long restaurantId = 201L;
        List<OrderItemModel> items = Collections.singletonList(new OrderItemModel());
        OrderStatus status = OrderStatus.PENDING;
        Long assignedEmployeeId = 301L;
        String securityPin = "1234";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now().plusHours(1);

        OrderModel order = new OrderModel(id, customerId, restaurantId, items, status, assignedEmployeeId, securityPin, createdAt, updatedAt);

        assertEquals(id, order.getId());
        assertEquals(customerId, order.getCustomerId());
        assertEquals(restaurantId, order.getRestaurantId());
        assertEquals(items, order.getItems());
        assertEquals(status, order.getStatus());
        assertEquals(assignedEmployeeId, order.getAssignedEmployeeId());
        assertEquals(securityPin, order.getSecurityPin());
        assertEquals(createdAt, order.getCreatedAt());
        assertEquals(updatedAt, order.getUpdatedAt());
    }

    @Test
    void shouldCreateOrderModelWithNoArgsConstructor() {
        OrderModel order = new OrderModel();
        assertNotNull(order);
    }

    @Test
    void shouldCreateOrderTraceModelWithAllArgsConstructor() {
        String id = "trace-123";
        Long orderId = 101L;
        Long customerId = 201L;
        Long restaurantId = 301L;
        Long employeeId = 401L;
        OrderStatus previousStatus = OrderStatus.PENDING;
        OrderStatus newStatus = OrderStatus.IN_PREPARATION;
        LocalDateTime timestamp = LocalDateTime.now();
        String notes = "Order started preparation";

        OrderTraceModel orderTrace = new OrderTraceModel(id, orderId, customerId, restaurantId, employeeId, previousStatus, newStatus, timestamp, notes);

        assertEquals(id, orderTrace.getId());
        assertEquals(orderId, orderTrace.getOrderId());
        assertEquals(customerId, orderTrace.getCustomerId());
        assertEquals(restaurantId, orderTrace.getRestaurantId());
        assertEquals(employeeId, orderTrace.getEmployeeId());
        assertEquals(previousStatus, orderTrace.getPreviousStatus());
        assertEquals(newStatus, orderTrace.getNewStatus());
        assertEquals(timestamp, orderTrace.getTimestamp());
        assertEquals(notes, orderTrace.getNotes());
    }

    @Test
    void shouldCreateOrderTraceModelWithNoArgsConstructor() {
        OrderTraceModel orderTrace = new OrderTraceModel();
        assertNotNull(orderTrace);
    }

    @Test
    void shouldSetAndGetAttributes() {
        OrderTraceModel orderTrace = new OrderTraceModel();
        String id = "trace-456";
        Long orderId = 102L;
        Long customerId = 202L;
        Long restaurantId = 302L;
        Long employeeId = 402L;
        OrderStatus previousStatus = OrderStatus.IN_PREPARATION;
        OrderStatus newStatus = OrderStatus.READY;
        LocalDateTime timestamp = LocalDateTime.now().plusHours(1);
        String notes = "Order is ready for delivery";

        orderTrace.setId(id);
        orderTrace.setOrderId(orderId);
        orderTrace.setCustomerId(customerId);
        orderTrace.setRestaurantId(restaurantId);
        orderTrace.setEmployeeId(employeeId);
        orderTrace.setPreviousStatus(previousStatus);
        orderTrace.setNewStatus(newStatus);
        orderTrace.setTimestamp(timestamp);
        orderTrace.setNotes(notes);

        assertEquals(id, orderTrace.getId());
        assertEquals(orderId, orderTrace.getOrderId());
        assertEquals(customerId, orderTrace.getCustomerId());
        assertEquals(restaurantId, orderTrace.getRestaurantId());
        assertEquals(employeeId, orderTrace.getEmployeeId());
        assertEquals(previousStatus, orderTrace.getPreviousStatus());
        assertEquals(newStatus, orderTrace.getNewStatus());
        assertEquals(timestamp, orderTrace.getTimestamp());
        assertEquals(notes, orderTrace.getNotes());
    }

}