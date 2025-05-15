package com.foodquart.microservicefoodcourt.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantEmployeeModelTest {

    @Test
    void shouldCreateRestaurantEmployeeModelWithAllArgsConstructor() {
        Long employeeId = 1L;
        String firstName = "Carlos";
        String lastName = "Pérez";
        String documentId = "123456789";
        String phone = "+573001122334";
        String email = "carlos.perez@example.com";
        String password = "securePassword";
        Long restaurantId = 101L;

        RestaurantEmployeeModel employee = new RestaurantEmployeeModel(employeeId, firstName, lastName, documentId, phone, email, password, restaurantId);

        assertEquals(employeeId, employee.getEmployeeId());
        assertEquals(firstName, employee.getFirstName());
        assertEquals(lastName, employee.getLastName());
        assertEquals(documentId, employee.getDocumentId());
        assertEquals(phone, employee.getPhone());
        assertEquals(email, employee.getEmail());
        assertEquals(password, employee.getPassword());
        assertEquals(restaurantId, employee.getRestaurantId());
    }

    @Test
    void shouldCreateRestaurantEmployeeModelWithNoArgsConstructor() {
        RestaurantEmployeeModel employee = new RestaurantEmployeeModel();
        assertNotNull(employee);
    }

    @Test
    void shouldSetAndGetEmployeeId() {
        RestaurantEmployeeModel employee = new RestaurantEmployeeModel();
        Long employeeId = 2L;
        employee.setEmployeeId(employeeId);
        assertEquals(employeeId, employee.getEmployeeId());
    }

    @Test
    void shouldSetAndGetFirstName() {
        RestaurantEmployeeModel employee = new RestaurantEmployeeModel();
        String firstName = "Ana";
        employee.setFirstName(firstName);
        assertEquals(firstName, employee.getFirstName());
    }

    @Test
    void shouldSetAndGetLastName() {
        RestaurantEmployeeModel employee = new RestaurantEmployeeModel();
        String lastName = "Gómez";
        employee.setLastName(lastName);
        assertEquals(lastName, employee.getLastName());
    }

    @Test
    void shouldSetAndGetDocumentId() {
        RestaurantEmployeeModel employee = new RestaurantEmployeeModel();
        String documentId = "987654321";
        employee.setDocumentId(documentId);
        assertEquals(documentId, employee.getDocumentId());
    }

    @Test
    void shouldSetAndGetPhone() {
        RestaurantEmployeeModel employee = new RestaurantEmployeeModel();
        String phone = "+573015556677";
        employee.setPhone(phone);
        assertEquals(phone, employee.getPhone());
    }

    @Test
    void shouldSetAndGetEmail() {
        RestaurantEmployeeModel employee = new RestaurantEmployeeModel();
        String email = "ana.gomez@example.com";
        employee.setEmail(email);
        assertEquals(email, employee.getEmail());
    }

    @Test
    void shouldSetAndGetPassword() {
        RestaurantEmployeeModel employee = new RestaurantEmployeeModel();
        String password = "anotherSecurePassword";
        employee.setPassword(password);
        assertEquals(password, employee.getPassword());
    }

    @Test
    void shouldSetAndGetRestaurantId() {
        RestaurantEmployeeModel employee = new RestaurantEmployeeModel();
        Long restaurantId = 102L;
        employee.setRestaurantId(restaurantId);
        assertEquals(restaurantId, employee.getRestaurantId());
    }
}