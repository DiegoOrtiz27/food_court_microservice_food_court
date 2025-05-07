package com.foodquart.microservicefoodcourt.domain.util;

public class DishMessages {
    public static final String DISH_CREATED = "The dish has been created successfully";
    public static final String DISH_UPDATED = "The dish has been updated successfully";
    public static final String DISH_ENABLED = "The dish has been enabled successfully";
    public static final String DISH_DISABLED = "The dish has been disabled successfully";
    public static final String DISH_NOT_FOUND = "Dish with id '%d' not found";
    public static final String DISH_NOT_ACTIVE = "Dish with id '%d' is not active";
    public static final String DISH_NOT_FROM_RESTAURANT = "Dish does not belong to the restaurant";

    public static final String PRICE_MUST_BE_POSITIVE = "Price must be positive";

    private DishMessages() {
        throw new AssertionError("Utility class should not be instantiated");
    }
}
