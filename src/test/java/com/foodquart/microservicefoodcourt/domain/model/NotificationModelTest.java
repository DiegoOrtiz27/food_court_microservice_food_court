package com.foodquart.microservicefoodcourt.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificationModelTest {

    @Test
    void shouldCreateNotificationModelWithAllArgsConstructor() {
        String phoneNumber = "+573009876543";
        String message = "Your order is being prepared.";

        NotificationModel notification = new NotificationModel(phoneNumber, message);

        assertEquals(phoneNumber, notification.getPhoneNumber());
        assertEquals(message, notification.getMessage());
    }

    @Test
    void shouldCreateNotificationModelWithNoArgsConstructor() {
        NotificationModel notification = new NotificationModel();
        assertNotNull(notification);
    }

    @Test
    void shouldSetAndGetPhoneNumber() {
        NotificationModel notification = new NotificationModel();
        String phoneNumber = "+573011122333";
        notification.setPhoneNumber(phoneNumber);
        assertEquals(phoneNumber, notification.getPhoneNumber());
    }

    @Test
    void shouldSetAndGetMessage() {
        NotificationModel notification = new NotificationModel();
        String message = "Your order is ready for delivery.";
        notification.setMessage(message);
        assertEquals(message, notification.getMessage());
    }

}