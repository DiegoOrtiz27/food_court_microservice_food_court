package com.foodquart.microservicefoodcourt.domain.usecase;

import com.foodquart.microservicefoodcourt.domain.exception.DomainException;
import com.foodquart.microservicefoodcourt.domain.model.RestaurantModel;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodquart.microservicefoodcourt.domain.util.Pagination;
import com.foodquart.microservicefoodcourt.domain.util.RestaurantMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @Mock
    IRestaurantPersistencePort restaurantPersistencePort;

    @InjectMocks
    RestaurantUseCase restaurantUseCase;

    private RestaurantModel validRestaurant;

    @BeforeEach
    void setup() {
        validRestaurant = new RestaurantModel();
        validRestaurant.setName("Sabor 123");
        validRestaurant.setNit("1234567890");
        validRestaurant.setAddress("Cra 45 #45-89");
        validRestaurant.setPhone("+573001234567");
        validRestaurant.setLogoUrl("https://logo.com");
        validRestaurant.setOwnerId(1L);
    }

    @Nested
    @DisplayName("saveRestaurant Tests")
    class SaveRestaurantTests {
        @Test
        @DisplayName("Should throw DomainException if name contains only digits")
        void saveRestaurantWhenNameOnlyDigitsThrowsDomainException() {
            validRestaurant.setName("12345");

            DomainException exception = assertThrows(DomainException.class,
                    () -> restaurantUseCase.saveRestaurant(validRestaurant));
            assertEquals(RestaurantMessages.NAME_NUMBERS_ONLY, exception.getMessage());

            verifyNoInteractions(restaurantPersistencePort);
        }

        @Test
        @DisplayName("Should throw DomainException if NIT is not numeric")
        void saveRestaurantWhenNitNotNumericThrowsDomainException() {
            validRestaurant.setNit("abc123");

            DomainException exception = assertThrows(DomainException.class,
                    () -> restaurantUseCase.saveRestaurant(validRestaurant));
            assertEquals(RestaurantMessages.NIT_NUMBERS_ONLY, exception.getMessage());

            verifyNoInteractions(restaurantPersistencePort);
        }

        @Test
        @DisplayName("Should throw DomainException if phone is too long")
        void saveRestaurantWhenPhoneTooLongThrowsDomainException() {
            validRestaurant.setPhone("+573001234567999");

            DomainException exception = assertThrows(DomainException.class,
                    () -> restaurantUseCase.saveRestaurant(validRestaurant));
            assertEquals(RestaurantMessages.INVALID_PHONE_FORMAT, exception.getMessage());

            verifyNoInteractions(restaurantPersistencePort);
        }

        @Test
        @DisplayName("Should throw NitAlreadyExistsException if NIT already exists")
        void saveRestaurantWhenNitExistsThrowsNitAlreadyExistsException() {
            when(restaurantPersistencePort.existsByNit(validRestaurant.getNit())).thenReturn(true);

            assertThrows(DomainException.class,
                    () -> restaurantUseCase.saveRestaurant(validRestaurant));

            verify(restaurantPersistencePort).existsByNit(validRestaurant.getNit());
            verifyNoMoreInteractions(restaurantPersistencePort);
        }

        @Test
        @DisplayName("Should throw DomainException if phone has invalid format (too short)")
        void saveRestaurantWhenPhoneTooShortThrowsDomainException() {
            validRestaurant.setPhone("123456789");

            DomainException exception = assertThrows(DomainException.class,
                    () -> restaurantUseCase.saveRestaurant(validRestaurant));
            assertEquals(RestaurantMessages.INVALID_PHONE_FORMAT, exception.getMessage());

            verifyNoInteractions(restaurantPersistencePort);
        }

        @Test
        @DisplayName("Should save restaurant successfully if all validations pass")
        void saveRestaurantWhenValidShouldSucceed() {
            when(restaurantPersistencePort.existsByNit(validRestaurant.getNit())).thenReturn(false);

            restaurantUseCase.saveRestaurant(validRestaurant);

            verify(restaurantPersistencePort).existsByNit(validRestaurant.getNit());
            verify(restaurantPersistencePort).saveRestaurant(validRestaurant);
        }
    }

    @Nested
    @DisplayName("getAllRestaurants Tests")
    class GetAllRestaurantsTests {

        @Test
        @DisplayName("Should return empty pagination when no restaurants exist")
        void getAllRestaurantsWhenNoRestaurantsShouldReturnEmptyPagination() {
            int page = 0;
            int size = 10;
            Pagination<RestaurantModel> emptyPagination = new Pagination<>(Collections.emptyList(), page, size, 0);
            when(restaurantPersistencePort.getAllRestaurants(page, size)).thenReturn(emptyPagination);

            Pagination<RestaurantModel> result = restaurantUseCase.getAllRestaurants(page, size);

            assertTrue(result.getItems().isEmpty());
            assertEquals(0, result.getTotalItems());
            verify(restaurantPersistencePort).getAllRestaurants(page, size);
        }

        @Test
        @DisplayName("Should return paginated restaurants when they exist")
        void getAllRestaurantsWhenRestaurantsExistShouldReturnPagination() {
            int page = 0;
            int size = 10;
            RestaurantModel restaurant = new RestaurantModel();
            restaurant.setName("Test Restaurant");
            Pagination<RestaurantModel> mockPagination = new Pagination<>(List.of(restaurant), page, size, 1);
            when(restaurantPersistencePort.getAllRestaurants(page, size)).thenReturn(mockPagination);

            Pagination<RestaurantModel> result = restaurantUseCase.getAllRestaurants(page, size);

            assertFalse(result.getItems().isEmpty());
            assertEquals(1, result.getItems().size());
            assertEquals("Test Restaurant", result.getItems().getFirst().getName());
            verify(restaurantPersistencePort).getAllRestaurants(page, size);
        }

        @Test
        @DisplayName("Should pass correct pagination parameters to persistence port")
        void getAllRestaurantsShouldPassCorrectPaginationParameters() {
            int page = 2;
            int size = 5;
            Pagination<RestaurantModel> mockPagination = new Pagination<>(Collections.emptyList(), page, size, 0);
            when(restaurantPersistencePort.getAllRestaurants(page, size)).thenReturn(mockPagination);

            Pagination<RestaurantModel> result = restaurantUseCase.getAllRestaurants(page, size);

            assertNotNull(result);
            verify(restaurantPersistencePort).getAllRestaurants(page, size);
        }

        @Test
        @DisplayName("Should validate pagination parameters")
        void getAllRestaurantsShouldValidatePaginationParameters() {
            assertThrows(DomainException.class, () -> restaurantUseCase.getAllRestaurants(-1, 10));
            assertThrows(DomainException.class, () -> restaurantUseCase.getAllRestaurants(0, 0));
            assertThrows(DomainException.class, () -> restaurantUseCase.getAllRestaurants(0, -1));
        }
    }
}