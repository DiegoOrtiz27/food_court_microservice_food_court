package com.foodquart.microservicefoodcourt.infrastructure.documentation;

public class APIRestaurantDocumentationConstant {

    public static final String CREATE_RESTAURANT_SUMMARY = "Create a new restaurant";
    public static final String CREATE_RESTAURANT_SUCCESS_DESCRIPTION = "Restaurant created successfully";
    public static final String CREATE_RESTAURANT_VALIDATION_ERROR_DESCRIPTION = "Validation error";
    public static final String CREATE_RESTAURANT_OWNER_NOT_VALID_DESCRIPTION = "Owner is not valid";

    public static final String GET_ALL_RESTAURANTS_SUMMARY = "Get all restaurants paginated and sorted by name";
    public static final String GET_ALL_RESTAURANTS_SUCCESS_DESCRIPTION = "Restaurants retrieved successfully";
    public static final String GET_ALL_RESTAURANTS_NO_CONTENT_DESCRIPTION = "No restaurants found";
    public static final String GET_ALL_RESTAURANTS_PAGE_PARAMETER_DESCRIPTION = "Page number (0-based)";
    public static final String GET_ALL_RESTAURANTS_SIZE_PARAMETER_DESCRIPTION = "Number of items per page";
    public static final String GET_ALL_RESTAURANTS_PAGE_PARAMETER_EXAMPLE = "0";
    public static final String GET_ALL_RESTAURANTS_SIZE_PARAMETER_EXAMPLE = "10";

    private APIRestaurantDocumentationConstant() {
    }
}
