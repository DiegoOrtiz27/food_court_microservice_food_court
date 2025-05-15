package com.foodquart.microservicefoodcourt.domain.util;

import com.foodquart.microservicefoodcourt.domain.exception.DomainException;
import com.foodquart.microservicefoodcourt.domain.model.OrderItemModel;
import com.foodquart.microservicefoodcourt.domain.model.OrderModel;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {

    private final Long validOrderId = 1L;
    private final Long validEmployeeId = 2L;
    private final Long validCustomerId = 3L;
    private final Long validRestaurantId = 4L;

    @Test
    void validateUpdateEmployeeFields_shouldNotThrowException_whenOrderIdAndEmployeeIdAreNotNull() {
        assertDoesNotThrow(() -> ValidationUtil.validateUpdateEmployeeFields(validOrderId, validEmployeeId));
    }

    @Test
    void validateUpdateEmployeeFields_shouldThrowException_whenOrderIdIsNull() {
        DomainException exception = assertThrows(DomainException.class,
                () -> ValidationUtil.validateUpdateEmployeeFields(null, validEmployeeId));
        assertTrue(exception.getMessage().contains(OrderMessages.ORDER_ID_REQUIRED));
    }

    @Test
    void validateUpdateEmployeeFields_shouldThrowException_whenEmployeeIdIsNull() {
        DomainException exception = assertThrows(DomainException.class,
                () -> ValidationUtil.validateUpdateEmployeeFields(validOrderId, null));
        assertTrue(exception.getMessage().contains(OrderMessages.EMPLOYEE_ID_REQUIRED));
    }

    @Test
    void validateUpdateCustomerFields_shouldNotThrowException_whenOrderIdAndCustomerIdAreNotNull() {
        assertDoesNotThrow(() -> ValidationUtil.validateUpdateCustomerFields(validOrderId, validCustomerId));
    }

    @Test
    void validateUpdateCustomerFields_shouldThrowException_whenOrderIdIsNull() {
        DomainException exception = assertThrows(DomainException.class,
                () -> ValidationUtil.validateUpdateCustomerFields(null, validCustomerId));
        assertTrue(exception.getMessage().contains(OrderMessages.ORDER_ID_REQUIRED));
    }

    @Test
    void validateUpdateCustomerFields_shouldThrowException_whenCustomerIdIsNull() {
        DomainException exception = assertThrows(DomainException.class,
                () -> ValidationUtil.validateUpdateCustomerFields(validOrderId, null));
        assertTrue(exception.getMessage().contains(OrderMessages.CUSTOMER_ID_REQUIRED));
    }

    @Test
    void validateOrderCreation_shouldNotThrowException_whenOrderModelIsValid() {
        OrderModel validOrder = new OrderModel();
        validOrder.setCustomerId(validCustomerId);
        validOrder.setRestaurantId(validRestaurantId);
        validOrder.setItems(Collections.singletonList(new OrderItemModel()));

        assertDoesNotThrow(() -> ValidationUtil.validateOrderCreation(validOrder));
    }

    @Test
    void validateOrderCreation_shouldNotThrowException_whenOrderHasMultipleItems() {
        OrderModel validOrder = new OrderModel();
        validOrder.setCustomerId(validCustomerId);
        validOrder.setRestaurantId(validRestaurantId);
        validOrder.setItems(List.of(new OrderItemModel(), new OrderItemModel()));

        assertDoesNotThrow(() -> ValidationUtil.validateOrderCreation(validOrder));
    }

    @Test
    void validateOrderCreation_shouldThrowException_whenCustomerIdIsNull() {
        OrderModel invalidOrder = new OrderModel();
        invalidOrder.setRestaurantId(validRestaurantId);
        invalidOrder.setItems(Collections.singletonList(new OrderItemModel()));

        DomainException exception = assertThrows(DomainException.class,
                () -> ValidationUtil.validateOrderCreation(invalidOrder));
        assertTrue(exception.getMessage().contains(OrderMessages.CUSTOMER_ID_REQUIRED));
    }

    @Test
    void validateOrderCreation_shouldThrowException_whenRestaurantIdIsNull() {
        OrderModel invalidOrder = new OrderModel();
        invalidOrder.setCustomerId(validCustomerId);
        invalidOrder.setItems(Collections.singletonList(new OrderItemModel()));

        DomainException exception = assertThrows(DomainException.class,
                () -> ValidationUtil.validateOrderCreation(invalidOrder));
        assertTrue(exception.getMessage().contains(OrderMessages.RESTAURANT_ID_REQUIRED));
    }

    @Test
    void validateOrderCreation_shouldThrowException_whenOrderItemsIsNull() {
        OrderModel invalidOrder = new OrderModel();
        invalidOrder.setCustomerId(validCustomerId);
        invalidOrder.setRestaurantId(validRestaurantId);
        invalidOrder.setItems(null);

        DomainException exception = assertThrows(DomainException.class,
                () -> ValidationUtil.validateOrderCreation(invalidOrder));
        assertTrue(exception.getMessage().contains(OrderMessages.ORDER_ITEMS_REQUIRED));
    }

    @Test
    void validateOrderCreation_shouldThrowException_whenOrderItemsIsEmpty() {
        OrderModel invalidOrder = new OrderModel();
        invalidOrder.setCustomerId(validCustomerId);
        invalidOrder.setRestaurantId(validRestaurantId);
        invalidOrder.setItems(Collections.emptyList());

        DomainException exception = assertThrows(DomainException.class,
                () -> ValidationUtil.validateOrderCreation(invalidOrder));
        assertTrue(exception.getMessage().contains(OrderMessages.ORDER_ITEMS_REQUIRED));
    }

    @Test
    void constructorShouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class, ValidationUtil::new, "Utility class");
    }
}