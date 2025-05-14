package com.foodquart.microservicefoodcourt.domain.usecase;

import com.foodquart.microservicefoodcourt.domain.exception.DomainException;
import com.foodquart.microservicefoodcourt.domain.model.DishModel;
import com.foodquart.microservicefoodcourt.domain.spi.IDishPersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.ISecurityContextPort;
import com.foodquart.microservicefoodcourt.domain.util.DishMessages;
import com.foodquart.microservicefoodcourt.domain.util.RestaurantMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishUseCaseTest {

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private ISecurityContextPort securityContextPort;

    @InjectMocks
    private DishUseCase dishUseCase;

    private DishModel validDish;
    private final Long ownerId = 10L;

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
        @DisplayName("Should throw DomainException when price is invalid")
        void shouldThrowDomainExceptionWhenPriceIsInvalid() {
            when(securityContextPort.getCurrentUserId()).thenReturn(ownerId);

            validDish.setPrice(0);

            DomainException exception = assertThrows(DomainException.class,
                    () -> dishUseCase.createDish(validDish));
            assertEquals(DishMessages.PRICE_MUST_BE_POSITIVE, exception.getMessage());

            verify(securityContextPort).getCurrentUserId();
            verifyNoMoreInteractions(restaurantPersistencePort, dishPersistencePort);
        }

        @Test
        @DisplayName("Should throw DomainException when restaurant does not exist")
        void shouldThrowInvalidRestaurantExceptionWhenRestaurantDoesNotExist() {
            when(securityContextPort.getCurrentUserId()).thenReturn(ownerId);
            when(restaurantPersistencePort.existsById(validDish.getRestaurantId())).thenReturn(false);

            DomainException exception = assertThrows(DomainException.class,
                    () -> dishUseCase.createDish(validDish));

            assertEquals(String.format(RestaurantMessages.RESTAURANT_NOT_FOUND, validDish.getRestaurantId()),
                    exception.getMessage());
            verify(securityContextPort).getCurrentUserId();
            verify(restaurantPersistencePort).existsById(validDish.getRestaurantId());
            verifyNoMoreInteractions(restaurantPersistencePort, dishPersistencePort);
        }

        @Test
        @DisplayName("Should throw DomainException when user is not owner")
        void shouldThrowInvalidOwnerExceptionWhenUserIsNotOwner() {
            when(securityContextPort.getCurrentUserId()).thenReturn(ownerId);
            when(restaurantPersistencePort.existsById(validDish.getRestaurantId())).thenReturn(true);
            when(restaurantPersistencePort.isOwnerOfRestaurant(ownerId, validDish.getRestaurantId())).thenReturn(false);

            DomainException exception = assertThrows(DomainException.class,
                    () -> dishUseCase.createDish(validDish));

            assertEquals(String.format(RestaurantMessages.OWNER_NOT_ASSOCIATED_TO_RESTAURANT,
                    validDish.getRestaurantId()), exception.getMessage());
            verify(securityContextPort).getCurrentUserId();
            verify(restaurantPersistencePort).existsById(validDish.getRestaurantId());
            verify(restaurantPersistencePort).isOwnerOfRestaurant(ownerId, validDish.getRestaurantId());
            verifyNoMoreInteractions(dishPersistencePort);
        }

        @Test
        @DisplayName("Should create dish successfully")
        void shouldCreateDishSuccessfully() {
            when(securityContextPort.getCurrentUserId()).thenReturn(ownerId);
            when(restaurantPersistencePort.existsById(validDish.getRestaurantId())).thenReturn(true);
            when(restaurantPersistencePort.isOwnerOfRestaurant(ownerId, validDish.getRestaurantId())).thenReturn(true);
            when(dishPersistencePort.saveDish(validDish)).thenReturn(validDish);

            DishModel result = dishUseCase.createDish(validDish);

            assertTrue(result.getActive());
            assertEquals(validDish, result);
            verify(securityContextPort).getCurrentUserId();
            verify(dishPersistencePort).saveDish(validDish);
            verify(restaurantPersistencePort).existsById(validDish.getRestaurantId());
            verify(restaurantPersistencePort).isOwnerOfRestaurant(ownerId, validDish.getRestaurantId());
        }
    }

    @Nested
    @DisplayName("Update Dish")
    class UpdateDish {

        @Test
        @DisplayName("Should throw DomainException when price is invalid")
        void shouldThrowDomainExceptionWhenPriceIsInvalid() {
            when(securityContextPort.getCurrentUserId()).thenReturn(ownerId);
            DishModel invalidDish = new DishModel();
            invalidDish.setPrice(0);

            DomainException exception = assertThrows(DomainException.class,
                    () -> dishUseCase.updateDish(1L, invalidDish));

            assertEquals(DishMessages.PRICE_MUST_BE_POSITIVE, exception.getMessage());
            verify(securityContextPort).getCurrentUserId();
            verifyNoInteractions(dishPersistencePort, restaurantPersistencePort);
        }

        @Test
        @DisplayName("Should throw DomainException when restaurant does not exist")
        void shouldThrowInvalidRestaurantExceptionWhenRestaurantDoesNotExist() {
            when(securityContextPort.getCurrentUserId()).thenReturn(ownerId);
            DishModel existingDish = new DishModel();
            existingDish.setId(1L);
            existingDish.setRestaurantId(5L);

            when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(existingDish));
            when(restaurantPersistencePort.existsById(existingDish.getRestaurantId())).thenReturn(false);

            DomainException exception = assertThrows(DomainException.class,
                    () -> dishUseCase.updateDish(1L, validDish));

            assertEquals(String.format(RestaurantMessages.RESTAURANT_NOT_FOUND, existingDish.getRestaurantId()),
                    exception.getMessage());
            verify(securityContextPort).getCurrentUserId();
            verify(dishPersistencePort).findById(1L);
            verify(restaurantPersistencePort).existsById(existingDish.getRestaurantId());
            verifyNoMoreInteractions(dishPersistencePort, restaurantPersistencePort);
        }

        @Test
        @DisplayName("Should throw DomainException when user is not owner")
        void shouldThrowInvalidOwnerExceptionWhenUserIsNotOwner() {
            when(securityContextPort.getCurrentUserId()).thenReturn(ownerId);
            DishModel existingDish = new DishModel();
            existingDish.setId(1L);
            existingDish.setRestaurantId(5L);

            when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(existingDish));
            when(restaurantPersistencePort.existsById(existingDish.getRestaurantId())).thenReturn(true);
            when(restaurantPersistencePort.isOwnerOfRestaurant(ownerId, existingDish.getRestaurantId())).thenReturn(false);

            DomainException exception = assertThrows(DomainException.class,
                    () -> dishUseCase.updateDish(1L, validDish));

            assertEquals(String.format(RestaurantMessages.OWNER_NOT_ASSOCIATED_TO_RESTAURANT,
                    existingDish.getRestaurantId()), exception.getMessage());
            verify(securityContextPort).getCurrentUserId();
            verify(dishPersistencePort).findById(1L);
            verify(restaurantPersistencePort).existsById(existingDish.getRestaurantId());
            verify(restaurantPersistencePort).isOwnerOfRestaurant(ownerId, existingDish.getRestaurantId());
            verifyNoMoreInteractions(dishPersistencePort);
        }

        @Test
        @DisplayName("Should update dish successfully")
        void shouldUpdateDishSuccessfully() {
            when(securityContextPort.getCurrentUserId()).thenReturn(ownerId);
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
            updatedDish.setDescription("Nueva descripción");
            updatedDish.setPrice(15000);

            when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(existingDish));
            when(restaurantPersistencePort.existsById(existingDish.getRestaurantId())).thenReturn(true);
            when(restaurantPersistencePort.isOwnerOfRestaurant(ownerId, existingDish.getRestaurantId())).thenReturn(true);
            when(dishPersistencePort.saveDish(existingDish)).thenReturn(existingDish);

            dishUseCase.updateDish(1L, updatedDish);

            assertEquals("Nueva descripción", existingDish.getDescription());
            assertEquals(15000, existingDish.getPrice());
            verify(securityContextPort).getCurrentUserId();
            verify(dishPersistencePort).findById(1L);
            verify(restaurantPersistencePort).existsById(existingDish.getRestaurantId());
            verify(restaurantPersistencePort).isOwnerOfRestaurant(ownerId, existingDish.getRestaurantId());
            verify(dishPersistencePort).saveDish(existingDish);
        }
    }

    @Nested
    @DisplayName("Enable or Disable Dish")
    class EnableOrDisableDish {

        @Test
        @DisplayName("Should throw DomainException when dish not found")
        void shouldThrowInvalidDishExceptionWhenDishNotFound() {
            when(securityContextPort.getCurrentUserId()).thenReturn(ownerId);
            when(dishPersistencePort.findById(1L)).thenReturn(Optional.empty());

            DomainException exception = assertThrows(DomainException.class,
                    () -> dishUseCase.enableOrDisableDish(1L, validDish));

            assertEquals(String.format(DishMessages.DISH_NOT_FOUND, 1L), exception.getMessage());
            verify(securityContextPort).getCurrentUserId();
            verify(dishPersistencePort).findById(1L);
            verifyNoMoreInteractions(restaurantPersistencePort, dishPersistencePort);
        }

        @Test
        @DisplayName("Should throw DomainException when restaurant does not exist")
        void shouldThrowInvalidRestaurantExceptionWhenRestaurantDoesNotExist() {
            when(securityContextPort.getCurrentUserId()).thenReturn(ownerId);
            DishModel existingDish = new DishModel();
            existingDish.setId(1L);
            existingDish.setRestaurantId(5L);
            existingDish.setActive(true);

            when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(existingDish));
            when(restaurantPersistencePort.existsById(existingDish.getRestaurantId())).thenReturn(false);

            DomainException exception = assertThrows(DomainException.class,
                    () -> dishUseCase.enableOrDisableDish(1L, validDish));

            assertEquals(String.format(RestaurantMessages.RESTAURANT_NOT_FOUND, existingDish.getRestaurantId()),
                    exception.getMessage());
            verify(securityContextPort).getCurrentUserId();
            verify(dishPersistencePort).findById(1L);
            verify(restaurantPersistencePort).existsById(existingDish.getRestaurantId());
            verifyNoMoreInteractions(dishPersistencePort);
        }

        @Test
        @DisplayName("Should throw DomainException when user is not owner")
        void shouldThrowInvalidOwnerExceptionWhenUserIsNotOwner() {
            when(securityContextPort.getCurrentUserId()).thenReturn(ownerId);
            DishModel existingDish = new DishModel();
            existingDish.setId(1L);
            existingDish.setRestaurantId(5L);
            existingDish.setActive(true);

            when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(existingDish));
            when(restaurantPersistencePort.existsById(existingDish.getRestaurantId())).thenReturn(true);
            when(restaurantPersistencePort.isOwnerOfRestaurant(ownerId, existingDish.getRestaurantId())).thenReturn(false);

            DomainException exception = assertThrows(DomainException.class,
                    () -> dishUseCase.enableOrDisableDish(1L, validDish));

            assertEquals(String.format(RestaurantMessages.OWNER_NOT_ASSOCIATED_TO_RESTAURANT,
                    existingDish.getRestaurantId()), exception.getMessage());
            verify(securityContextPort).getCurrentUserId();
            verify(dishPersistencePort).findById(1L);
            verify(restaurantPersistencePort).existsById(existingDish.getRestaurantId());
            verify(restaurantPersistencePort).isOwnerOfRestaurant(ownerId, existingDish.getRestaurantId());
            verifyNoMoreInteractions(dishPersistencePort);
        }

        @Test
        @DisplayName("Should enable dish successfully")
        void shouldEnableDishSuccessfully() {
            when(securityContextPort.getCurrentUserId()).thenReturn(ownerId);
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
            when(dishPersistencePort.saveDish(existingDish)).thenReturn(existingDish);

            DishModel result = dishUseCase.enableOrDisableDish(1L, dishToEnable);

            assertTrue(result.getActive());
            verify(securityContextPort).getCurrentUserId();
            verify(dishPersistencePort).findById(1L);
            verify(restaurantPersistencePort).existsById(existingDish.getRestaurantId());
            verify(restaurantPersistencePort).isOwnerOfRestaurant(ownerId, existingDish.getRestaurantId());
            verify(dishPersistencePort).saveDish(existingDish);
        }

        @Test
        @DisplayName("Should disable dish successfully")
        void shouldDisableDishSuccessfully() {
            when(securityContextPort.getCurrentUserId()).thenReturn(ownerId);
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
            when(dishPersistencePort.saveDish(existingDish)).thenReturn(existingDish);

            DishModel result = dishUseCase.enableOrDisableDish(1L, dishToDisable);

            assertFalse(result.getActive());
            verify(securityContextPort).getCurrentUserId();
            verify(dishPersistencePort).findById(1L);
            verify(restaurantPersistencePort).existsById(existingDish.getRestaurantId());
            verify(restaurantPersistencePort).isOwnerOfRestaurant(ownerId, existingDish.getRestaurantId());
            verify(dishPersistencePort).saveDish(existingDish);
        }
    }

    @Nested
    @DisplayName("Get Dishes By Restaurant")
    class GetDishesByRestaurant {

        @Test
        @DisplayName("Should throw DomainException when restaurant does not exist")
        void shouldThrowExceptionWhenRestaurantNotFound() {
            Long restaurantId = 1L;
            when(restaurantPersistencePort.existsById(restaurantId)).thenReturn(false);

            DomainException exception = assertThrows(DomainException.class,
                    () -> dishUseCase.getDishesByRestaurant(restaurantId, "Starter", 0, 10));

            assertEquals(String.format(RestaurantMessages.RESTAURANT_NOT_FOUND, restaurantId),
                    exception.getMessage());
            verify(restaurantPersistencePort).existsById(restaurantId);
            verifyNoInteractions(securityContextPort, dishPersistencePort);
        }

        @Test
        @DisplayName("Should return page of dishes when restaurant exists")
        void shouldReturnPageOfDishesWhenRestaurantExists() {
            Long restaurantId = 1L;
            Page<DishModel> expectedPage = new PageImpl<>(List.of(validDish));

            when(restaurantPersistencePort.existsById(restaurantId)).thenReturn(true);
            when(dishPersistencePort.findByRestaurantIdAndCategory(restaurantId, "Starter", 0, 10))
                    .thenReturn(expectedPage);

            Page<DishModel> result = dishUseCase.getDishesByRestaurant(restaurantId, "Starter", 0, 10);

            assertEquals(expectedPage, result);
            verify(restaurantPersistencePort).existsById(restaurantId);
            verify(dishPersistencePort).findByRestaurantIdAndCategory(restaurantId, "Starter", 0, 10);
            verifyNoInteractions(securityContextPort);
        }
    }
}