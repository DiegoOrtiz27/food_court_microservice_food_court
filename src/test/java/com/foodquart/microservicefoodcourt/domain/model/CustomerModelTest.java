package com.foodquart.microservicefoodcourt.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerModelTest {

    @Test
    void shouldCreateCustomerModelWithAllArgsConstructor() {
        Long customerId = 123L;
        String phone = "+573001234567";

        CustomerModel customer = new CustomerModel(customerId, phone);

        assertEquals(customerId, customer.getCustomerId());
        assertEquals(phone, customer.getPhone());
    }

    @Test
    void shouldCreateCustomerModelWithNoArgsConstructor() {
        CustomerModel customer = new CustomerModel();
        assertNotNull(customer);
    }

    @Test
    void shouldSetAndGetCustomerId() {
        CustomerModel customer = new CustomerModel();
        Long customerId = 456L;
        customer.setCustomerId(customerId);
        assertEquals(customerId, customer.getCustomerId());
    }

    @Test
    void shouldSetAndGetPhone() {
        CustomerModel customer = new CustomerModel();
        String phone = "+573019876543";
        customer.setPhone(phone);
        assertEquals(phone, customer.getPhone());
    }

}