package com.foodquart.microservicefoodcourt.infrastructure.documentation;

public class APIDishDocumentationConstant {

    public static final String CREATE_DISH_SUMMARY = "Create a new dish";
    public static final String CREATE_DISH_SUCCESS_DESCRIPTION = "Dish created successfully";
    public static final String CREATE_DISH_INVALID_INPUT_DESCRIPTION = "Invalid input data";
    public static final String CREATE_DISH_UNAUTHORIZED_DESCRIPTION = "Only restaurant owners can create dishes";

    public static final String UPDATE_DISH_SUMMARY = "Update a dish (price and description only)";
    public static final String UPDATE_DISH_SUCCESS_DESCRIPTION = "Dish updated successfully";
    public static final String UPDATE_DISH_INVALID_DATA_DESCRIPTION = "Invalid data";
    public static final String UPDATE_DISH_UNAUTHORIZED_DESCRIPTION = "Not authorized";

    public static final String ENABLE_DISABLE_DISH_SUMMARY = "Enable or disable a dish";
    public static final String ENABLE_DISABLE_DISH_SUCCESS_DESCRIPTION = "Dish status updated successfully";
    public static final String ENABLE_DISABLE_DISH_INVALID_INPUT_DESCRIPTION = "Invalid input data";
    public static final String ENABLE_DISABLE_DISH_UNAUTHORIZED_DESCRIPTION = "Not authorized";

    public static final String GET_DISHES_BY_RESTAURANT_SUMMARY = "Get dishes by restaurant with pagination and category filter";
    public static final String GET_DISHES_BY_RESTAURANT_SUCCESS_DESCRIPTION = "Dishes retrieved successfully";
    public static final String GET_DISHES_BY_RESTAURANT_NOT_FOUND_DESCRIPTION = "Restaurant not found";

    private APIDishDocumentationConstant() {
    }
}
