package com.foodquart.microservicefoodcourt.domain.util;

import com.foodquart.microservicefoodcourt.domain.exception.DomainException;
import com.foodquart.microservicefoodcourt.domain.model.RestaurantModel;

public class ValidationUtil {

    public static final String PHONE_REGEX = "^\\+?\\d{10,13}$";
    public static final String NUMERIC_REGEX = "\\d+";

    public static void validateRestaurantData(RestaurantModel restaurant) {
        if (restaurant.getName().matches(NUMERIC_REGEX)) {
            throw new DomainException(RestaurantMessages.NAME_NUMBERS_ONLY);
        }
        if (!restaurant.getNit().matches(NUMERIC_REGEX)) {
            throw new DomainException(RestaurantMessages.NIT_NUMBERS_ONLY);
        }
        if (!restaurant.getPhone().matches(PHONE_REGEX)) {
            throw new DomainException(RestaurantMessages.INVALID_PHONE_FORMAT);
        }
    }

    private ValidationUtil() {
        throw new AssertionError("Utility class should not be instantiated");
    }
}
