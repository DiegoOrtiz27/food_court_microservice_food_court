package com.foodquart.microservicefoodcourt.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DishModelTest {
    @Test
    void shouldCreateDishModelWithAllArgsConstructor() {
        Long id = 1L;
        String name = "Pizza Margherita";
        Integer price = 25000;
        String description = "Classic tomato and mozzarella pizza";
        String imageUrl = "http://example.com/pizza.jpg";
        String category = "Italian";
        Boolean active = true;
        Long restaurantId = 101L;

        DishModel dish = new DishModel(id, name, price, description, imageUrl, category, active, restaurantId);

        assertEquals(id, dish.getId());
        assertEquals(name, dish.getName());
        assertEquals(price, dish.getPrice());
        assertEquals(description, dish.getDescription());
        assertEquals(imageUrl, dish.getImageUrl());
        assertEquals(category, dish.getCategory());
        assertEquals(active, dish.getActive());
        assertEquals(restaurantId, dish.getRestaurantId());
    }

    @Test
    void shouldCreateDishModelWithNoArgsConstructor() {
        DishModel dish = new DishModel();
        assertNotNull(dish);
    }

    @Test
    void shouldSetAndGetId() {
        DishModel dish = new DishModel();
        Long id = 2L;
        dish.setId(id);
        assertEquals(id, dish.getId());
    }

    @Test
    void shouldSetAndGetName() {
        DishModel dish = new DishModel();
        String name = "Pasta Carbonara";
        dish.setName(name);
        assertEquals(name, dish.getName());
    }

    @Test
    void shouldSetAndGetPrice() {
        DishModel dish = new DishModel();
        Integer price = 20000;
        dish.setPrice(price);
        assertEquals(price, dish.getPrice());
    }

    @Test
    void shouldSetAndGetDescription() {
        DishModel dish = new DishModel();
        String description = "Creamy pasta with bacon and eggs";
        dish.setDescription(description);
        assertEquals(description, dish.getDescription());
    }

    @Test
    void shouldSetAndGetImageUrl() {
        DishModel dish = new DishModel();
        String imageUrl = "http://example.com/pasta.jpg";
        dish.setImageUrl(imageUrl);
        assertEquals(imageUrl, dish.getImageUrl());
    }

    @Test
    void shouldSetAndGetCategory() {
        DishModel dish = new DishModel();
        String category = "Italian";
        dish.setCategory(category);
        assertEquals(category, dish.getCategory());
    }

    @Test
    void shouldSetAndGetActive() {
        DishModel dish = new DishModel();
        Boolean active = false;
        dish.setActive(active);
        assertEquals(active, dish.getActive());
    }

    @Test
    void shouldSetAndGetRestaurantId() {
        DishModel dish = new DishModel();
        Long restaurantId = 102L;
        dish.setRestaurantId(restaurantId);
        assertEquals(restaurantId, dish.getRestaurantId());
    }
}