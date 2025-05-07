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

    @InjectMocks
    DishUseCase dishUseCase;

    private DishModel validDish;
    private Long ownerId;

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
        ownerId = 10L;
    }

    @Nested
    @DisplayName("Create Dish")
    class CreateDish {

        @Test
        void shouldThrowDomainExceptionWhenPriceIsInvalid() {
            validDish.setPrice(0);

            DomainException exception = assertThrows(DomainException.class,
                    () -> dishUseCase.createDish(validDish, ownerId));
            assertEquals("Price must be positive", exception.getMessage());

            verifyNoInteractions(restaurantPersistencePort, dishPersistencePort);
        }

        @Test
        void shouldThrowInvalidRestaurantExceptionWhenRestaurantDoesNotExist() {
            when(restaurantPersistencePort.existsById(validDish.getRestaurantId())).thenReturn(false);

            InvalidRestaurantException exception = assertThrows(InvalidRestaurantException.class,
                    () -> dishUseCase.createDish(validDish, ownerId));

            assertEquals("The restaurant with id '5' does not exists", exception.getMessage());
            verify(restaurantPersistencePort).existsById(validDish.getRestaurantId());
            verifyNoMoreInteractions(restaurantPersistencePort, dishPersistencePort);
        }

        @Test
        void shouldThrowInvalidOwnerExceptionWhenUserIsNotOwner() {
            when(restaurantPersistencePort.existsById(validDish.getRestaurantId())).thenReturn(true);
            when(restaurantPersistencePort.isOwnerOfRestaurant(ownerId, validDish.getRestaurantId())).thenReturn(false);

            InvalidOwnerException exception = assertThrows(InvalidOwnerException.class,
                    () -> dishUseCase.createDish(validDish, ownerId));

            assertEquals("User is not the owner of restaurant with ID 5", exception.getMessage());
            verify(restaurantPersistencePort).existsById(validDish.getRestaurantId());
            verify(restaurantPersistencePort).isOwnerOfRestaurant(ownerId, validDish.getRestaurantId());
            verifyNoMoreInteractions(restaurantPersistencePort, dishPersistencePort);
        }

        @Test
        void shouldCreateDishSuccessfully() {
            when(restaurantPersistencePort.existsById(validDish.getRestaurantId())).thenReturn(true);
            when(restaurantPersistencePort.isOwnerOfRestaurant(ownerId, validDish.getRestaurantId())).thenReturn(true);
            when(dishPersistencePort.saveDish(validDish)).thenReturn(validDish);

            DishModel result = dishUseCase.createDish(validDish, ownerId);

            assertTrue(result.getActive());
            assertEquals(validDish, result);
            verify(dishPersistencePort).saveDish(validDish);
            verify(restaurantPersistencePort).existsById(validDish.getRestaurantId());
            verify(restaurantPersistencePort).isOwnerOfRestaurant(ownerId, validDish.getRestaurantId());
        }
    }

    @Nested
    @DisplayName("Update Dish")
    class UpdateDish {

        @Test
        void shouldThrowDomainExceptionWhenPriceIsInvalid() {
            validDish.setPrice(0);

            DomainException exception = assertThrows(DomainException.class,
                    () -> dishUseCase.updateDish(validDish, ownerId));

            assertEquals("Price must be positive", exception.getMessage());
            verifyNoInteractions(dishPersistencePort, restaurantPersistencePort);
        }

        @Test
        void shouldThrowInvalidRestaurantExceptionWhenRestaurantDoesNotExist() {
            DishModel existingDish = new DishModel();
            existingDish.setId(1L);
            existingDish.setRestaurantId(5L);
            when(dishPersistencePort.findById(validDish.getId())).thenReturn(Optional.of(existingDish));
            when(restaurantPersistencePort.existsById(existingDish.getRestaurantId())).thenReturn(false);

            InvalidRestaurantException exception = assertThrows(InvalidRestaurantException.class,
                    () -> dishUseCase.updateDish(validDish, ownerId));

            assertEquals("The restaurant with id '5' does not exists", exception.getMessage());
            verify(dishPersistencePort).findById(validDish.getId());
            verify(restaurantPersistencePort).existsById(existingDish.getRestaurantId());
            verifyNoMoreInteractions(dishPersistencePort, restaurantPersistencePort);
        }

        @Test
        void shouldThrowInvalidOwnerExceptionWhenUserIsNotOwner() {
            DishModel existingDish = new DishModel();
            existingDish.setId(1L);
            existingDish.setRestaurantId(5L);
            when(dishPersistencePort.findById(validDish.getId())).thenReturn(Optional.of(existingDish));
            when(restaurantPersistencePort.existsById(existingDish.getRestaurantId())).thenReturn(true);
            when(restaurantPersistencePort.isOwnerOfRestaurant(ownerId, existingDish.getRestaurantId())).thenReturn(false);

            InvalidOwnerException exception = assertThrows(InvalidOwnerException.class,
                    () -> dishUseCase.updateDish(validDish, ownerId));

            // Mensaje de error corregido
            assertEquals("User is not the owner of restaurant with ID 5", exception.getMessage());
            verify(dishPersistencePort).findById(validDish.getId());
            verify(restaurantPersistencePort).existsById(existingDish.getRestaurantId());
            verify(restaurantPersistencePort).isOwnerOfRestaurant(ownerId, existingDish.getRestaurantId());
            verifyNoMoreInteractions(dishPersistencePort, restaurantPersistencePort);
        }

        @Test
        void shouldUpdateDishSuccessfully() {
            DishModel existingDish = new DishModel();
            existingDish.setId(1L);
            existingDish.setName("Ensalada César");
            existingDish.setPrice(12000);
            existingDish.setDescription("Fresca y saludable");
            existingDish.setImageUrl("https://img.com/ensalada.jpg");
            existingDish.setCategory("Starter");
            existingDish.setRestaurantId(5L);
            existingDish.setActive(true);

            DishModel updatedDish = new DishModel();
            updatedDish.setId(1L);
            updatedDish.setDescription("Nueva descripción");
            updatedDish.setPrice(15000);

            when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(existingDish));
            when(restaurantPersistencePort.existsById(existingDish.getRestaurantId())).thenReturn(true);
            when(restaurantPersistencePort.isOwnerOfRestaurant(ownerId, existingDish.getRestaurantId())).thenReturn(true);
            when(dishPersistencePort.updateDish(existingDish)).thenReturn(existingDish);

            dishUseCase.updateDish(updatedDish, ownerId);

            assertEquals("Nueva descripción", existingDish.getDescription());
            assertEquals(15000, existingDish.getPrice());
            verify(dishPersistencePort).findById(1L);
            verify(restaurantPersistencePort).existsById(existingDish.getRestaurantId());
            verify(restaurantPersistencePort).isOwnerOfRestaurant(ownerId, existingDish.getRestaurantId());
            verify(dishPersistencePort).updateDish(existingDish);
        }
    }

    @Nested
    @DisplayName("Enable or Disable Dish")
    class EnableOrDisableDish {

        @Test
        void shouldThrowInvalidDishExceptionWhenDishNotFound() {
            when(dishPersistencePort.findById(validDish.getId())).thenReturn(Optional.empty());

            InvalidDishException exception = assertThrows(InvalidDishException.class,
                    () -> dishUseCase.enableOrDisableDish(validDish, ownerId));

            assertEquals("Dish with id '1' not found", exception.getMessage());
            verify(dishPersistencePort).findById(validDish.getId());
            verifyNoMoreInteractions(dishPersistencePort, restaurantPersistencePort);
        }

        @Test
        void shouldThrowInvalidRestaurantExceptionWhenRestaurantDoesNotExist() {
            DishModel existingDish = new DishModel();
            existingDish.setId(1L);
            existingDish.setRestaurantId(5L);
            existingDish.setActive(true);

            when(dishPersistencePort.findById(validDish.getId())).thenReturn(Optional.of(existingDish));
            when(restaurantPersistencePort.existsById(existingDish.getRestaurantId())).thenReturn(false);

            InvalidRestaurantException exception = assertThrows(InvalidRestaurantException.class,
                    () -> dishUseCase.enableOrDisableDish(validDish, ownerId));

            assertEquals("The restaurant with id '5' does not exists", exception.getMessage());
            verify(dishPersistencePort).findById(validDish.getId());
            verify(restaurantPersistencePort).existsById(existingDish.getRestaurantId());
            verifyNoMoreInteractions(dishPersistencePort, restaurantPersistencePort);
        }

        @Test
        void shouldThrowInvalidOwnerExceptionWhenUserIsNotOwner() {
            DishModel existingDish = new DishModel();
            existingDish.setId(1L);
            existingDish.setRestaurantId(5L);
            existingDish.setActive(true);

            when(dishPersistencePort.findById(validDish.getId())).thenReturn(Optional.of(existingDish));
            when(restaurantPersistencePort.existsById(existingDish.getRestaurantId())).thenReturn(true);
            when(restaurantPersistencePort.isOwnerOfRestaurant(ownerId, existingDish.getRestaurantId())).thenReturn(false);

            InvalidOwnerException exception = assertThrows(InvalidOwnerException.class,
                    () -> dishUseCase.enableOrDisableDish(validDish, ownerId));

            assertEquals("User is not the owner of restaurant with ID 5", exception.getMessage());
            verify(dishPersistencePort).findById(validDish.getId());
            verify(restaurantPersistencePort).existsById(existingDish.getRestaurantId());
            verify(restaurantPersistencePort).isOwnerOfRestaurant(ownerId, existingDish.getRestaurantId());
            verifyNoMoreInteractions(dishPersistencePort, restaurantPersistencePort);
        }

        @Test
        void shouldEnableDishSuccessfully() {
            DishModel existingDish = new DishModel();
            existingDish.setId(1L);
            existingDish.setName("Ensalada César");
            existingDish.setRestaurantId(5L);
            existingDish.setActive(false);

            DishModel dishToEnable = new DishModel();
            dishToEnable.setId(1L);
            dishToEnable.setActive(true);

            when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(existingDish));
            when(restaurantPersistencePort.existsById(existingDish.getRestaurantId())).thenReturn(true);
            when(restaurantPersistencePort.isOwnerOfRestaurant(ownerId, existingDish.getRestaurantId())).thenReturn(true);
            when(dishPersistencePort.updateDishStatus(existingDish)).thenReturn(existingDish);

            DishModel result = dishUseCase.enableOrDisableDish(dishToEnable, ownerId);

            assertTrue(result.getActive());
            verify(dishPersistencePort).findById(1L);
            verify(restaurantPersistencePort).existsById(existingDish.getRestaurantId());
            verify(restaurantPersistencePort).isOwnerOfRestaurant(ownerId, existingDish.getRestaurantId());
            verify(dishPersistencePort).updateDishStatus(existingDish);
        }

        @Test
        void shouldDisableDishSuccessfully() {
            DishModel existingDish = new DishModel();
            existingDish.setId(1L);
            existingDish.setName("Ensalada César");
            existingDish.setRestaurantId(5L);
            existingDish.setActive(true);

            DishModel dishToDisable = new DishModel();
            dishToDisable.setId(1L);
            dishToDisable.setActive(false);

            when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(existingDish));
            when(restaurantPersistencePort.existsById(existingDish.getRestaurantId())).thenReturn(true);
            when(restaurantPersistencePort.isOwnerOfRestaurant(ownerId, existingDish.getRestaurantId())).thenReturn(true);
            when(dishPersistencePort.updateDishStatus(existingDish)).thenReturn(existingDish);

            DishModel result = dishUseCase.enableOrDisableDish(dishToDisable, ownerId);

            assertFalse(result.getActive());
            verify(dishPersistencePort).findById(1L);
            verify(restaurantPersistencePort).existsById(existingDish.getRestaurantId());
            verify(restaurantPersistencePort).isOwnerOfRestaurant(ownerId, existingDish.getRestaurantId());
            verify(dishPersistencePort).updateDishStatus(existingDish);
        }
    }
}