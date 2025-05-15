package com.foodquart.microservicefoodcourt.domain.util;

public class RestaurantMessages {

    public static final String RESTAURANT_CREATED = "The restaurant has been created successfully";
    public static final String EMPLOYEE_CREATED = "The employee has been created successfully";
    public static final String RESTAURANT_NOT_FOUND = "Restaurant with id '%d' not found";
    public static final String EMPLOYEE_NOT_ASSOCIATED_TO_RESTAURANT = "Employee is not associated with restaurant with id '%d'";
    public static final String OWNER_NOT_ASSOCIATED_TO_RESTAURANT = "User is not the owner of restaurant with id '%d'";
    public static final String NAME_NUMBERS_ONLY = "Restaurant name cannot contain only numbers";
    public static final String NIT_NUMBERS_ONLY = "NIT must contain only numbers";
    public static final String INVALID_PHONE_FORMAT = "Phone should have maximum 13 characters and can include '+'";
    public static final String NIT_ALREADY_EXISTS = "Restaurant with NIT '%s' already exists";

    RestaurantMessages() {
        throw new IllegalStateException("Utility class");
    }
}
