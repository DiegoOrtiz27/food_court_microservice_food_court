package com.foodquart.microservicefoodcourt.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemModelTest {
    @Test
    void shouldCreateOrderItemModelWithAllArgsConstructor() {
        Long id = 1L;
        int quantity = 2;
        DishModel dish = new DishModel();
        dish.setId(100L);
        dish.setName("Hamburguesa");
        dish.setPrice(15000);
        dish.setDescription("Deliciosa hamburguesa de carne");
        dish.setImageUrl("http://example.com/hamburguesa.jpg");
        dish.setRestaurantId(1L);
        dish.setCategory("TEST");
        dish.setActive(true);

        OrderItemModel orderItem = new OrderItemModel(id, quantity, dish);

        assertEquals(id, orderItem.getId());
        assertEquals(quantity, orderItem.getQuantity());
        assertEquals(dish, orderItem.getDish());
    }

    @Test
    void shouldCreateOrderItemModelWithNoArgsConstructor() {
        OrderItemModel orderItem = new OrderItemModel();
        assertNotNull(orderItem);
    }

    @Test
    void shouldSetAndGetId() {
        OrderItemModel orderItem = new OrderItemModel();
        Long id = 2L;
        orderItem.setId(id);
        assertEquals(id, orderItem.getId());
    }

    @Test
    void shouldSetAndGetQuantity() {
        OrderItemModel orderItem = new OrderItemModel();
        int quantity = 3;
        orderItem.setQuantity(quantity);
        assertEquals(quantity, orderItem.getQuantity());
    }

    @Test
    void shouldSetAndGetDish() {
        OrderItemModel orderItem = new OrderItemModel();
        DishModel dish = new DishModel();
        dish.setName("Pizza");
        orderItem.setDish(dish);
        assertEquals(dish, orderItem.getDish());
    }

}