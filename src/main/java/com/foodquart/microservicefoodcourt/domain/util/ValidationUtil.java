package com.foodquart.microservicefoodcourt.domain.util;

import com.foodquart.microservicefoodcourt.domain.exception.DomainException;
import com.foodquart.microservicefoodcourt.domain.model.DishModel;
import com.foodquart.microservicefoodcourt.domain.model.OrderModel;
import com.foodquart.microservicefoodcourt.domain.model.RestaurantModel;
import com.foodquart.microservicefoodcourt.domain.spi.IDishPersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantEmployeePersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantPersistencePort;

import java.util.Optional;

import static com.foodquart.microservicefoodcourt.domain.util.DishMessages.DISH_NOT_FOUND;
import static com.foodquart.microservicefoodcourt.domain.util.DishMessages.PRICE_MUST_BE_POSITIVE;
import static com.foodquart.microservicefoodcourt.domain.util.OrderMessages.*;
import static com.foodquart.microservicefoodcourt.domain.util.RestaurantMessages.*;

public class ValidationUtil {

    public static final String PHONE_REGEX = "^\\+?\\d{10,13}$";
    public static final String NUMERIC_REGEX = "\\d+";

    public static void validateRestaurantData(RestaurantModel restaurant) {
        if (restaurant.getName().matches(NUMERIC_REGEX)) {
            throw new DomainException(NAME_NUMBERS_ONLY);
        }
        if (!restaurant.getNit().matches(NUMERIC_REGEX)) {
            throw new DomainException(NIT_NUMBERS_ONLY);
        }
        if (!restaurant.getPhone().matches(PHONE_REGEX)) {
            throw new DomainException(INVALID_PHONE_FORMAT);
        }
    }

    public static void validateOrderCreation(OrderModel orderModel) throws DomainException {
        if (orderModel.getCustomerId() == null) {
            throw new DomainException(CUSTOMER_ID_REQUIRED);
        }

        if (orderModel.getRestaurantId() == null) {
            throw new DomainException(RESTAURANT_ID_REQUIRED);
        }

        if (orderModel.getItems() == null || orderModel.getItems().isEmpty()) {
            throw new DomainException(ORDER_ITEMS_REQUIRED);
        }
    }

    public static void validateUpdateEmployeeFields(Long orderId, Long employeeId) {
        if (orderId == null) {
            throw new DomainException(ORDER_ID_REQUIRED);
        }
        if (employeeId == null) {
            throw new DomainException(EMPLOYEE_ID_REQUIRED);
        }
    }

    public static void validateUpdateCustomerFields(Long orderId, Long customerId) {
        if (orderId == null) {
            throw new DomainException(ORDER_ID_REQUIRED);
        }
        if (customerId == null) {
            throw new DomainException(CUSTOMER_ID_REQUIRED);
        }
    }

    public static void validateDeliveryFields(Long orderId, Long employeeId, String securityPin) {
        validateUpdateEmployeeFields(orderId, employeeId);
        if (securityPin == null || securityPin.trim().isEmpty()) {
            throw new DomainException(SECURITY_PIN_REQUIRED);
        }
    }

    public static void validateSecurityPin(String inputPin, String storedPin) {
        if (!inputPin.equals(storedPin)) {
            throw new DomainException(INVALID_SECURITY_PIN);
        }
    }

    public static void validatePagination(int page, int size) {
        if (page < 0) {
            throw new DomainException("Page number must not be less than zero");
        }
            if (size < 1) {
            throw new DomainException("Page size must not be less than one");
        }
    }

    public static void validateDish(DishModel dish) {
        if (dish.getPrice() <= 0) {
            throw new DomainException(PRICE_MUST_BE_POSITIVE);
        }
    }

    public static void existsRestaurantById(IRestaurantPersistencePort restaurantPersistencePort, Long restaurantId) {
        if (!restaurantPersistencePort.existsById(restaurantId)) {
            throw new DomainException(String.format(RESTAURANT_NOT_FOUND, restaurantId));
        }
    }

    public static void existsByEmployeeIdAndRestaurantId(IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort, Long restaurantId, Long employeeId) {
        if (!restaurantEmployeePersistencePort.existsByEmployeeIdAndRestaurantId(employeeId, restaurantId)) {
            throw new DomainException(String.format(EMPLOYEE_NOT_ASSOCIATED_TO_RESTAURANT, restaurantId));
        }
    }

    public static void isOwnerOfRestaurant(IRestaurantPersistencePort restaurantPersistencePort, Long restaurantId, Long ownerId) {
        if (!restaurantPersistencePort.isOwnerOfRestaurant(ownerId, restaurantId)) {
            throw new DomainException(String.format(OWNER_NOT_ASSOCIATED_TO_RESTAURANT, restaurantId));
        }
    }

    public static DishModel validateExistingDish(IDishPersistencePort dishPersistencePort, Long dishId) {
        Optional<DishModel> existingDish = dishPersistencePort.findById(dishId);
        if(existingDish.isEmpty()) {
            throw new DomainException(String.format(DISH_NOT_FOUND, dishId));
        }
        return existingDish.get();
    }

    ValidationUtil() {
        throw new IllegalStateException("Utility class");
    }
}
