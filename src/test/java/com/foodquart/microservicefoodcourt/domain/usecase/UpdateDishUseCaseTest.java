package com.foodquart.microservicefoodcourt.domain.usecase;

import com.foodquart.microservicefoodcourt.domain.exception.DomainException;
import com.foodquart.microservicefoodcourt.domain.exception.InvalidDishException;
import com.foodquart.microservicefoodcourt.domain.exception.InvalidOwnerException;
import com.foodquart.microservicefoodcourt.domain.exception.InvalidRestaurantException;
import com.foodquart.microservicefoodcourt.domain.model.DishModel;
import com.foodquart.microservicefoodcourt.domain.spi.IDishPersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateDishUseCaseTest {

    @Mock
    IDishPersistencePort dishPersistencePort;

    @Mock
    IRestaurantPersistencePort restaurantPersistencePort;

    @InjectMocks
    UpdateDishUseCase updateDishUseCase;

    private DishModel existingDish;

    @BeforeEach
    void setup() {
        existingDish = new DishModel();
        existingDish.setId(1L);
        existingDish.setPrice(12000);
        existingDish.setDescription("Original description");
        existingDish.setRestaurantId(5L);
    }

    @Test
    @DisplayName("Should throw DomainException when price is invalid")
    void updateDishWithInvalidPriceThrowsDomainException() {
        DomainException exception = assertThrows(DomainException.class,
                () -> updateDishUseCase.updateDish(1L, "new description", 0, 1L));

        assertEquals("Price must be positive", exception.getMessage());

        verifyNoInteractions(dishPersistencePort, restaurantPersistencePort);
    }

    @Test
    @DisplayName("Should throw InvalidDishException when dish does not exist")
    void updateDishWhenDishDoesNotExistThrowsException() {
        when(dishPersistencePort.findById(1L)).thenReturn(Optional.empty());

        InvalidDishException exception = assertThrows(InvalidDishException.class,
                () -> updateDishUseCase.updateDish(1L, "new description", 15000, 1L));

        assertEquals("Dish with id '1' not found.", exception.getMessage());

        verify(dishPersistencePort).findById(1L);
        verifyNoMoreInteractions(dishPersistencePort, restaurantPersistencePort);
    }

    @Test
    @DisplayName("Should throw InvalidRestaurantException when restaurant does not exist")
    void updateDishWhenRestaurantDoesNotExistThrowsException() {
        when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(existingDish));
        when(restaurantPersistencePort.existsById(5L)).thenReturn(false);

        InvalidRestaurantException exception = assertThrows(InvalidRestaurantException.class,
                () -> updateDishUseCase.updateDish(1L, "new description", 15000, 1L));

        assertEquals("The restaurant with id '5' does not exists", exception.getMessage());

        InOrder inOrder = inOrder(dishPersistencePort, restaurantPersistencePort);
        inOrder.verify(dishPersistencePort).findById(1L);
        inOrder.verify(restaurantPersistencePort).existsById(5L);
        verifyNoMoreInteractions(dishPersistencePort, restaurantPersistencePort);
    }

    @Test
    @DisplayName("Should throw InvalidOwnerException when user is not the owner")
    void updateDishWhenNotOwnerThrowsException() {
        when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(existingDish));
        when(restaurantPersistencePort.existsById(5L)).thenReturn(true);
        when(restaurantPersistencePort.isOwnerOfRestaurant(1L, 5L)).thenReturn(false);

        InvalidOwnerException exception = assertThrows(InvalidOwnerException.class,
                () -> updateDishUseCase.updateDish(1L, "new description", 15000, 1L));

        assertEquals("User with ID 1 is not the owner of restaurant with ID 5", exception.getMessage());

        InOrder inOrder = inOrder(dishPersistencePort, restaurantPersistencePort);
        inOrder.verify(dishPersistencePort).findById(1L);
        inOrder.verify(restaurantPersistencePort).existsById(5L);
        inOrder.verify(restaurantPersistencePort).isOwnerOfRestaurant(1L, 5L);
        verifyNoMoreInteractions(dishPersistencePort, restaurantPersistencePort);
    }

    @Test
    @DisplayName("Should update dish successfully when all validations pass")
    void updateDishWhenValidShouldSucceed() {
        when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(existingDish));
        when(restaurantPersistencePort.existsById(5L)).thenReturn(true);
        when(restaurantPersistencePort.isOwnerOfRestaurant(1L, 5L)).thenReturn(true);

        updateDishUseCase.updateDish(1L, "New updated description", 16000, 1L);

        assertEquals("New updated description", existingDish.getDescription());
        assertEquals(16000, existingDish.getPrice());

        InOrder inOrder = inOrder(dishPersistencePort, restaurantPersistencePort);
        inOrder.verify(dishPersistencePort).findById(1L);
        inOrder.verify(restaurantPersistencePort).existsById(5L);
        inOrder.verify(restaurantPersistencePort).isOwnerOfRestaurant(1L, 5L);
        inOrder.verify(dishPersistencePort).updateDish(existingDish);
    }
}