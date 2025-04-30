package com.foodquart.microservicefoodcourt.domain.usecase;

import com.foodquart.microservicefoodcourt.domain.exception.DomainException;
import com.foodquart.microservicefoodcourt.domain.exception.InvalidOwnerException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateDishUseCaseTest {

    @Mock
    IDishPersistencePort dishPersistencePort;

    @Mock
    IRestaurantPersistencePort restaurantPersistencePort;

    @InjectMocks
    CreateDishUseCase createDishUseCase;

    private DishModel validDish;

    @BeforeEach
    void setup() {
        validDish = new DishModel();
        validDish.setName("Ensalada César");
        validDish.setPrice(12000);
        validDish.setDescription("Fresca, saludable y deliciosa");
        validDish.setImageUrl("https://img.com/ensalada.jpg");
        validDish.setCategory("Starter");
        validDish.setRestaurantId(5L);
    }

    @Test
    @DisplayName("Should throw DomainException when price is zero or negative")
    void createDishWithInvalidPriceThrowsDomainException() {
        validDish.setPrice(0);

        DomainException exception = assertThrows(DomainException.class,
                () -> createDishUseCase.createDish(validDish, 1L));
        assertEquals("Price must be positive", exception.getMessage());

        verifyNoInteractions(restaurantPersistencePort, dishPersistencePort);
    }

    @Test
    @DisplayName("Should throw InvalidOwnerException when user is not owner of restaurant")
    void createDishWhenUserIsNotOwnerThrowsInvalidOwnerException() {
        when(restaurantPersistencePort.isOwnerOfRestaurant(1L, validDish.getRestaurantId())).thenReturn(false);

        assertThrows(InvalidOwnerException.class,
                () -> createDishUseCase.createDish(validDish, 1L));

        verify(restaurantPersistencePort).isOwnerOfRestaurant(1L, validDish.getRestaurantId());
        verifyNoMoreInteractions(restaurantPersistencePort, dishPersistencePort);
    }

    @Test
    @DisplayName("Should create dish successfully when validations pass")
    void createDishWhenValidShouldSucceed() {
        when(restaurantPersistencePort.isOwnerOfRestaurant(1L, validDish.getRestaurantId())).thenReturn(true);
        when(dishPersistencePort.saveDish(validDish)).thenReturn(validDish);

        DishModel result = createDishUseCase.createDish(validDish, 1L);

        assertEquals(validDish, result);
        assertTrue(result.getActive());

        InOrder inOrder = inOrder(restaurantPersistencePort, dishPersistencePort);
        inOrder.verify(restaurantPersistencePort).isOwnerOfRestaurant(1L, validDish.getRestaurantId());
        inOrder.verify(dishPersistencePort).saveDish(validDish);
    }
}