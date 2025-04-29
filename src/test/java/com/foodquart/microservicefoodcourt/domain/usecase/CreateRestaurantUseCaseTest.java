package com.foodquart.microservicefoodcourt.domain.usecase;

import com.foodquart.microservicefoodcourt.domain.exception.DomainException;
import com.foodquart.microservicefoodcourt.domain.exception.InvalidOwnerRoleException;
import com.foodquart.microservicefoodcourt.domain.exception.NitAlreadyExistsException;
import com.foodquart.microservicefoodcourt.domain.model.RestaurantModel;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IUserClientPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateRestaurantUseCaseTest {

    @Mock
    IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    IUserClientPort userClientPort;

    @InjectMocks
    CreateRestaurantUseCase createRestaurantUseCase;

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

    @Test
    @DisplayName("Should throw DomainException if name contains only digits")
    void saveRestaurantWhenNameOnlyDigitsThrowsDomainException() {
        validRestaurant.setName("12345");

        DomainException exception = assertThrows(DomainException.class,
                () -> createRestaurantUseCase.saveRestaurant(validRestaurant));
        assertEquals("Restaurant name cannot contain only numbers.", exception.getMessage());

        verifyNoInteractions(restaurantPersistencePort, userClientPort);
    }

    @Test
    @DisplayName("Should throw DomainException if NIT is not numeric")
    void saveRestaurantWhenNitNotNumericThrowsDomainException() {
        validRestaurant.setNit("abc123");

        DomainException exception = assertThrows(DomainException.class,
                () -> createRestaurantUseCase.saveRestaurant(validRestaurant));
        assertEquals("NIT must contain only numbers.", exception.getMessage());

        verifyNoInteractions(restaurantPersistencePort, userClientPort);
    }

    @Test
    @DisplayName("Should throw DomainException if phone is too long")
    void saveRestaurantWhenPhoneTooLongThrowsDomainException() {
        validRestaurant.setPhone("+573001234567999");

        DomainException exception = assertThrows(DomainException.class,
                () -> createRestaurantUseCase.saveRestaurant(validRestaurant));
        assertEquals("Phone must be numeric and up to 13 characters.", exception.getMessage());

        verifyNoInteractions(restaurantPersistencePort, userClientPort);
    }

    @Test
    @DisplayName("Should throw NitAlreadyExistsException if NIT already exists")
    void saveRestaurantWhenNitExistsThrowsNitAlreadyExistsException() {
        when(restaurantPersistencePort.existsByNit(validRestaurant.getNit())).thenReturn(true);

        assertThrows(NitAlreadyExistsException.class,
                () -> createRestaurantUseCase.saveRestaurant(validRestaurant));

        verify(restaurantPersistencePort).existsByNit(validRestaurant.getNit());
        verifyNoMoreInteractions(restaurantPersistencePort, userClientPort);
    }

    @Test
    @DisplayName("Should throw InvalidOwnerRoleException if user is not owner")
    void saveRestaurantWhenUserIsNotOwnerThrowsInvalidOwnerRoleException() {
        when(restaurantPersistencePort.existsByNit(validRestaurant.getNit())).thenReturn(false);
        when(userClientPort.findOwnerById(validRestaurant.getOwnerId())).thenReturn(false);

        assertThrows(InvalidOwnerRoleException.class,
                () -> createRestaurantUseCase.saveRestaurant(validRestaurant));

        InOrder inOrder = inOrder(restaurantPersistencePort, userClientPort);
        inOrder.verify(restaurantPersistencePort).existsByNit(validRestaurant.getNit());
        inOrder.verify(userClientPort).findOwnerById(validRestaurant.getOwnerId());

        verifyNoMoreInteractions(restaurantPersistencePort, userClientPort);
    }

    @Test
    @DisplayName("Should save restaurant successfully if all validations pass")
    void saveRestaurantWhenValidShouldSucceed() {
        when(restaurantPersistencePort.existsByNit(validRestaurant.getNit())).thenReturn(false);
        when(userClientPort.findOwnerById(validRestaurant.getOwnerId())).thenReturn(true);

        createRestaurantUseCase.saveRestaurant(validRestaurant);

        InOrder inOrder = inOrder(restaurantPersistencePort, userClientPort);
        inOrder.verify(restaurantPersistencePort).existsByNit(validRestaurant.getNit());
        inOrder.verify(userClientPort).findOwnerById(validRestaurant.getOwnerId());
        inOrder.verify(restaurantPersistencePort).saveRestaurant(validRestaurant);
    }
}