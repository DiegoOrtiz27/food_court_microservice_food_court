package com.foodquart.microservicefoodcourt.domain.util;

import com.foodquart.microservicefoodcourt.domain.exception.DomainException;
import com.foodquart.microservicefoodcourt.domain.model.OrderModel;
import com.foodquart.microservicefoodcourt.domain.model.RestaurantModel;

public class ValidationUtil {

    public static final String PHONE_REGEX = "^\\+?\\d{10,13}$";
    public static final String NUMERIC_REGEX = "\\d+";

    public static void validateRestaurantData(RestaurantModel restaurant) {
        if (restaurant.getName().matches(NUMERIC_REGEX)) {
            throw new DomainException(RestaurantMessages.NAME_NUMBERS_ONLY);
        }
        if (!restaurant.getNit().matches(NUMERIC_REGEX)) {
            throw new DomainException(RestaurantMessages.NIT_NUMBERS_ONLY);
        }
        if (!restaurant.getPhone().matches(PHONE_REGEX)) {
            throw new DomainException(RestaurantMessages.INVALID_PHONE_FORMAT);
        }
    }

    public static void validateOrderCreation(OrderModel orderModel) throws DomainException {
        if (orderModel.getCustomerId() == null) {
            throw new DomainException(OrderMessages.CUSTOMER_ID_REQUIRED);
        }

        if (orderModel.getRestaurantId() == null) {
            throw new DomainException(OrderMessages.RESTAURANT_ID_REQUIRED);
        }

        if (orderModel.getItems() == null || orderModel.getItems().isEmpty()) {
            throw new DomainException(OrderMessages.ORDER_ITEMS_REQUIRED);
        }
    }

    public static void validateUpdateFields(Long orderId, Long employeeId) {
        if (orderId == null) {
            throw new DomainException(OrderMessages.ORDER_ID_REQUIRED);
        }
        if (employeeId == null) {
            throw new DomainException(OrderMessages.EMPLOYEE_ID_REQUIRED);
        }
    }

    public static void validateDeliveryFields(Long orderId, Long employeeId, String securityPin) {
        validateUpdateFields(orderId, employeeId);
        if (securityPin == null || securityPin.trim().isEmpty()) {
            throw new DomainException(OrderMessages.SECURITY_PIN_REQUIRED);
        }
    }

    public static void validateSecurityPin(String inputPin, String storedPin) {
        if (!inputPin.equals(storedPin)) {
            throw new DomainException(OrderMessages.INVALID_SECURITY_PIN);
        }
    }

    private ValidationUtil() {
        throw new AssertionError("Utility class should not be instantiated");
    }
}
