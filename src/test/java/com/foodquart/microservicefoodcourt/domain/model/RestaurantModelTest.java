package com.foodquart.microservicefoodcourt.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantModelTest {

    @Test
    void shouldCreateRestaurantModelWithAllArgsConstructor() {
        Long id = 1L;
        String name = "El Rancherito";
        String nit = "900123456-7";
        String address = "Calle 10 # 40-50";
        String phone = "+573009876543";
        String logoUrl = "http://example.com/rancherito.jpg";
        Long ownerId = 201L;

        RestaurantModel restaurant = new RestaurantModel(id, name, nit, address, phone, logoUrl, ownerId);

        assertEquals(id, restaurant.getId());
        assertEquals(name, restaurant.getName());
        assertEquals(nit, restaurant.getNit());
        assertEquals(address, restaurant.getAddress());
        assertEquals(phone, restaurant.getPhone());
        assertEquals(logoUrl, restaurant.getLogoUrl());
        assertEquals(ownerId, restaurant.getOwnerId());
    }

    @Test
    void shouldCreateRestaurantModelWithNoArgsConstructor() {
        RestaurantModel restaurant = new RestaurantModel();
        assertNotNull(restaurant);
    }

    @Test
    void shouldSetAndGetId() {
        RestaurantModel restaurant = new RestaurantModel();
        Long id = 2L;
        restaurant.setId(id);
        assertEquals(id, restaurant.getId());
    }

    @Test
    void shouldSetAndGetName() {
        RestaurantModel restaurant = new RestaurantModel();
        String name = "La Fogata";
        restaurant.setName(name);
        assertEquals(name, restaurant.getName());
    }

    @Test
    void shouldSetAndGetNit() {
        RestaurantModel restaurant = new RestaurantModel();
        String nit = "800987654-3";
        restaurant.setNit(nit);
        assertEquals(nit, restaurant.getNit());
    }

    @Test
    void shouldSetAndGetAddress() {
        RestaurantModel restaurant = new RestaurantModel();
        String address = "Avenida 30 # 5-15";
        restaurant.setAddress(address);
        assertEquals(address, restaurant.getAddress());
    }

    @Test
    void shouldSetAndGetPhone() {
        RestaurantModel restaurant = new RestaurantModel();
        String phone = "+573011122333";
        restaurant.setPhone(phone);
        assertEquals(phone, restaurant.getPhone());
    }

    @Test
    void shouldSetAndGetLogoUrl() {
        RestaurantModel restaurant = new RestaurantModel();
        String logoUrl = "http://example.com/lafogata.png";
        restaurant.setLogoUrl(logoUrl);
        assertEquals(logoUrl, restaurant.getLogoUrl());
    }

    @Test
    void shouldSetAndGetOwnerId() {
        RestaurantModel restaurant = new RestaurantModel();
        Long ownerId = 202L;
        restaurant.setOwnerId(ownerId);
        assertEquals(ownerId, restaurant.getOwnerId());
    }
}