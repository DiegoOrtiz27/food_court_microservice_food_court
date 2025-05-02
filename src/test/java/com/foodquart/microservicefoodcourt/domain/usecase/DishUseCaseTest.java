package com.foodquart.microservicefoodcourt.domain.usecase;

import com.foodquart.microservicefoodcourt.domain.exception.DomainException;
import com.foodquart.microservicefoodcourt.domain.exception.InvalidDishException;
import com.foodquart.microservicefoodcourt.domain.exception.InvalidOwnerException;
import com.foodquart.microservicefoodcourt.domain.exception.InvalidRestaurantException;
import com.foodquart.microservicefoodcourt.domain.model.DishModel;
import com.foodquart.microservicefoodcourt.domain.spi.IDishPersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IUserClientPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishUseCaseTest {

    @Mock
    IDishPersistencePort dishPersistencePort;

    @Mock
    IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    IUserClientPort userClientPort;

    @InjectMocks
    DishUseCase dishUseCase;

    private DishModel validDish;

    @BeforeEach
    void setup() {
        validDish = new DishModel();
        validDish.setId(1L);
        validDish.setName("Ensalada César");
        validDish.setPrice(12000);
        validDish.setDescription("Fresca y saludable");
        validDish.setImageUrl("https://img.com/ensalada.jpg");
        validDish.setCategory("Starter");
        validDish.setRestaurantId(5L);
    }

    @Nested
    @DisplayName("Create Dish")
    class CreateDish {

        @Test
        void shouldThrowDomainExceptionWhenPriceIsInvalid() {
            validDish.setPrice(0);

            DomainException exception = assertThrows(DomainException.class,
                    () -> dishUseCase.createDish(validDish));
            assertEquals("Price must be positive", exception.getMessage());

            verifyNoInteractions(restaurantPersistencePort, userClientPort, dishPersistencePort);
        }

        @Test
        void shouldThrowInvalidRestaurantExceptionWhenRestaurantDoesNotExist() {
            when(restaurantPersistencePort.existsById(validDish.getRestaurantId())).thenReturn(false);

            InvalidRestaurantException exception = assertThrows(InvalidRestaurantException.class,
                    () -> dishUseCase.createDish(validDish));

            assertEquals("The restaurant with id '5' does not exists", exception.getMessage());
            verify(restaurantPersistencePort).existsById(validDish.getRestaurantId());
            verifyNoMoreInteractions(restaurantPersistencePort, userClientPort, dishPersistencePort);
        }

        @Test
        void shouldThrowInvalidOwnerExceptionWhenUserIsNotOwner() {
            when(restaurantPersistencePort.existsById(validDish.getRestaurantId())).thenReturn(true);
            when(userClientPort.getUserId()).thenReturn(1L);
            when(restaurantPersistencePort.isOwnerOfRestaurant(1L, validDish.getRestaurantId())).thenReturn(false);

            InvalidOwnerException exception = assertThrows(InvalidOwnerException.class,
                    () -> dishUseCase.createDish(validDish));

            assertEquals("User with ID 1 is not the owner of restaurant with ID 5", exception.getMessage());
        }

        @Test
        void shouldCreateDishSuccessfully() {
            when(restaurantPersistencePort.existsById(validDish.getRestaurantId())).thenReturn(true);
            when(userClientPort.getUserId()).thenReturn(1L);
            when(restaurantPersistencePort.isOwnerOfRestaurant(1L, validDish.getRestaurantId())).thenReturn(true);
            when(dishPersistencePort.saveDish(validDish)).thenReturn(validDish);

            DishModel result = dishUseCase.createDish(validDish);

            assertTrue(result.getActive());
            assertEquals(validDish, result);
            verify(dishPersistencePort).saveDish(validDish);
        }
    }

    @Nested
    @DisplayName("Update Dish")
    class UpdateDish {

        @Test
        void shouldThrowDomainExceptionWhenPriceIsInvalid() {
            validDish.setPrice(0);

            DomainException exception = assertThrows(DomainException.class,
                    () -> dishUseCase.updateDish(validDish));

            assertEquals("Price must be positive", exception.getMessage());
            verifyNoInteractions(dishPersistencePort, restaurantPersistencePort, userClientPort);
        }

        @Test
        void shouldThrowInvalidDishExceptionWhenDishNotFound() {
            when(dishPersistencePort.findById(validDish.getId())).thenReturn(Optional.empty());

            InvalidDishException exception = assertThrows(InvalidDishException.class,
                    () -> dishUseCase.updateDish(validDish));

            assertEquals("Dish with id '1' not found", exception.getMessage());
        }

        @Test
        void shouldThrowInvalidOwnerExceptionWhenUserIsNotOwner() {
            when(dishPersistencePort.findById(validDish.getId())).thenReturn(Optional.of(validDish));
            when(restaurantPersistencePort.existsById(validDish.getRestaurantId())).thenReturn(true);
            when(userClientPort.getUserId()).thenReturn(1L);
            when(restaurantPersistencePort.isOwnerOfRestaurant(1L, validDish.getRestaurantId())).thenReturn(false);

            InvalidOwnerException exception = assertThrows(InvalidOwnerException.class,
                    () -> dishUseCase.updateDish(validDish));

            assertEquals("User with ID 1 is not the owner of restaurant with ID 5", exception.getMessage());
        }

        @Test
        void shouldUpdateDishSuccessfully() {
            DishModel updatedDish = new DishModel();
            updatedDish.setId(1L);
            updatedDish.setDescription("Nueva descripción");
            updatedDish.setPrice(15000);

            when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(validDish));
            when(restaurantPersistencePort.existsById(validDish.getRestaurantId())).thenReturn(true);
            when(userClientPort.getUserId()).thenReturn(1L);
            when(restaurantPersistencePort.isOwnerOfRestaurant(1L, validDish.getRestaurantId())).thenReturn(true);

            dishUseCase.updateDish(updatedDish);

            assertEquals("Nueva descripción", validDish.getDescription());
            assertEquals(15000, validDish.getPrice());
            verify(dishPersistencePort).updateDish(validDish);
        }
    }
}