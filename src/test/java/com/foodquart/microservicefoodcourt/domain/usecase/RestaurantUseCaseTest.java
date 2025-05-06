package com.foodquart.microservicefoodcourt.domain.usecase;

import com.foodquart.microservicefoodcourt.domain.exception.DomainException;
import com.foodquart.microservicefoodcourt.domain.exception.NitAlreadyExistsException;
import com.foodquart.microservicefoodcourt.domain.model.RestaurantModel;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        validRestaurant.setOwnerId(1L); // Aunque no se use en este caso de uso
    }

    @Test
    @DisplayName("Should throw DomainException if name contains only digits")
    void saveRestaurantWhenNameOnlyDigitsThrowsDomainException() {
        validRestaurant.setName("12345");

        DomainException exception = assertThrows(DomainException.class,
                () -> restaurantUseCase.saveRestaurant(validRestaurant));
        assertEquals("Restaurant name cannot contain only numbers.", exception.getMessage());

        verifyNoInteractions(restaurantPersistencePort);
    }

    @Test
    @DisplayName("Should throw DomainException if NIT is not numeric")
    void saveRestaurantWhenNitNotNumericThrowsDomainException() {
        validRestaurant.setNit("abc123");

        DomainException exception = assertThrows(DomainException.class,
                () -> restaurantUseCase.saveRestaurant(validRestaurant));
        assertEquals("NIT must contain only numbers.", exception.getMessage());

        verifyNoInteractions(restaurantPersistencePort);
    }

    @Test
    @DisplayName("Should throw DomainException if phone is too long")
    void saveRestaurantWhenPhoneTooLongThrowsDomainException() {
        validRestaurant.setPhone("+573001234567999");

        DomainException exception = assertThrows(DomainException.class,
                () -> restaurantUseCase.saveRestaurant(validRestaurant));
        assertEquals("Phone should have maximum 13 characters and can include '+'", exception.getMessage());

        verifyNoInteractions(restaurantPersistencePort);
    }

    @Test
    @DisplayName("Should throw NitAlreadyExistsException if NIT already exists")
    void saveRestaurantWhenNitExistsThrowsNitAlreadyExistsException() {
        when(restaurantPersistencePort.existsByNit(validRestaurant.getNit())).thenReturn(true);

        assertThrows(NitAlreadyExistsException.class,
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
        assertEquals("Phone should have maximum 13 characters and can include '+'", exception.getMessage());

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