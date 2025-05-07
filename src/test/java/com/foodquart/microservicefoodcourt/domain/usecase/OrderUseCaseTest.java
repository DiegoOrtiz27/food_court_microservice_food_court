package com.foodquart.microservicefoodcourt.domain.usecase;

import com.foodquart.microservicefoodcourt.domain.exception.DomainException;
import com.foodquart.microservicefoodcourt.domain.model.DishModel;
import com.foodquart.microservicefoodcourt.domain.model.OrderItemModel;
import com.foodquart.microservicefoodcourt.domain.model.OrderModel;
import com.foodquart.microservicefoodcourt.domain.spi.IDishPersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IOrderPersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodquart.microservicefoodcourt.domain.util.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderUseCaseTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @InjectMocks
    private OrderUseCase orderUseCase;

    private OrderModel validOrder;
    private final Long validCustomerId = 1L;
    private final Long validRestaurantId = 5L;
    private final Long validDishId1 = 10L;
    private final Long validDishId2 = 11L;

    @BeforeEach
    void setup() {
        validOrder = new OrderModel();
        validOrder.setCustomerId(validCustomerId);
        validOrder.setRestaurantId(validRestaurantId);
        validOrder.setItems(List.of(
                new OrderItemModel(validDishId1, 2),
                new OrderItemModel(validDishId2, 1)
        ));
    }

    @Test
    @DisplayName("Should create order successfully with valid data")
    void shouldCreateOrderSuccessfullyWithValidData() {
        when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
        when(orderPersistencePort.hasActiveOrders(validCustomerId)).thenReturn(false);

        DishModel dish1 = new DishModel();
        dish1.setId(validDishId1);
        dish1.setActive(true);
        dish1.setRestaurantId(validRestaurantId);

        DishModel dish2 = new DishModel();
        dish2.setId(validDishId2);
        dish2.setActive(true);
        dish2.setRestaurantId(validRestaurantId);

        when(dishPersistencePort.findById(validDishId1)).thenReturn(java.util.Optional.of(dish1));
        when(dishPersistencePort.findById(validDishId2)).thenReturn(java.util.Optional.of(dish2));

        OrderModel savedOrder = new OrderModel();
        savedOrder.setStatus(OrderStatus.PENDING);
        when(orderPersistencePort.saveOrder(any(OrderModel.class))).thenReturn(savedOrder);

        OrderModel result = orderUseCase.createOrder(validOrder);

        assertNotNull(result);
        assertEquals(OrderStatus.PENDING, result.getStatus());

        verify(restaurantPersistencePort).existsById(validRestaurantId);
        verify(orderPersistencePort).hasActiveOrders(validCustomerId);
        verify(dishPersistencePort).findById(validDishId1);
        verify(dishPersistencePort).findById(validDishId2);
        verify(orderPersistencePort).saveOrder(any(OrderModel.class));
    }

    @Test
    @DisplayName("Should fail when restaurant does not exist")
    void shouldFailWhenRestaurantDoesNotExist() {
        when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(false);

        DomainException exception = assertThrows(DomainException.class,
                () -> orderUseCase.createOrder(validOrder));

        assertEquals("Restaurant with id '5' not found", exception.getMessage());
        verify(restaurantPersistencePort).existsById(validRestaurantId);
        verifyNoInteractions(dishPersistencePort, orderPersistencePort);
    }

    @Test
    @DisplayName("Should fail when customer has active orders")
    void shouldFailWhenCustomerHasActiveOrders() {
        when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
        when(orderPersistencePort.hasActiveOrders(validCustomerId)).thenReturn(true);

        DomainException exception = assertThrows(DomainException.class,
                () -> orderUseCase.createOrder(validOrder));

        assertEquals("Customer has an active order in progress", exception.getMessage());
        verify(restaurantPersistencePort).existsById(validRestaurantId);
        verify(orderPersistencePort).hasActiveOrders(validCustomerId);
        verifyNoInteractions(dishPersistencePort);
    }

    @Test
    @DisplayName("Should fail when dish does not exist")
    void shouldFailWhenDishDoesNotExist() {
        when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
        when(orderPersistencePort.hasActiveOrders(validCustomerId)).thenReturn(false);
        when(dishPersistencePort.findById(validDishId1)).thenReturn(java.util.Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> orderUseCase.createOrder(validOrder));

        assertEquals("Dish with id '10' not found", exception.getMessage());
        verify(dishPersistencePort).findById(validDishId1);
        verifyNoMoreInteractions(dishPersistencePort);
    }

    @Test
    @DisplayName("Should fail when dish is not active")
    void shouldFailWhenDishIsNotActive() {
        when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
        when(orderPersistencePort.hasActiveOrders(validCustomerId)).thenReturn(false);

        DishModel inactiveDish = new DishModel();
        inactiveDish.setId(validDishId1);
        inactiveDish.setActive(false);
        inactiveDish.setRestaurantId(validRestaurantId);

        when(dishPersistencePort.findById(validDishId1)).thenReturn(java.util.Optional.of(inactiveDish));

        DomainException exception = assertThrows(DomainException.class,
                () -> orderUseCase.createOrder(validOrder));

        assertEquals("Dish with id '10' is not active", exception.getMessage());
        verify(dishPersistencePort).findById(validDishId1);
    }

    @Test
    @DisplayName("Should fail when dish does not belong to restaurant")
    void shouldFailWhenDishDoesNotBelongToRestaurant() {
        when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
        when(orderPersistencePort.hasActiveOrders(validCustomerId)).thenReturn(false);

        DishModel wrongRestaurantDish = new DishModel();
        wrongRestaurantDish.setId(validDishId1);
        wrongRestaurantDish.setActive(true);
        wrongRestaurantDish.setRestaurantId(99L);

        when(dishPersistencePort.findById(validDishId1)).thenReturn(java.util.Optional.of(wrongRestaurantDish));

        DomainException exception = assertThrows(DomainException.class,
                () -> orderUseCase.createOrder(validOrder));

        assertEquals("Dish does not belong to the restaurant", exception.getMessage());
        verify(dishPersistencePort).findById(validDishId1);
    }

    @Test
    @DisplayName("Should set status to PENDING when creating order")
    void shouldSetStatusToPendingWhenCreatingOrder() {
        when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
        when(orderPersistencePort.hasActiveOrders(validCustomerId)).thenReturn(false);

        DishModel dish1 = new DishModel();
        dish1.setId(validDishId1);
        dish1.setActive(true);
        dish1.setRestaurantId(validRestaurantId);

        DishModel dish2 = new DishModel();
        dish2.setId(validDishId2);
        dish2.setActive(true);
        dish2.setRestaurantId(validRestaurantId);

        when(dishPersistencePort.findById(validDishId1)).thenReturn(java.util.Optional.of(dish1));
        when(dishPersistencePort.findById(validDishId2)).thenReturn(java.util.Optional.of(dish2));

        OrderModel savedOrder = new OrderModel();
        savedOrder.setStatus(OrderStatus.PENDING);
        when(orderPersistencePort.saveOrder(argThat(order ->
                order.getStatus() == OrderStatus.PENDING
        ))).thenReturn(savedOrder);

        OrderModel result = orderUseCase.createOrder(validOrder);

        assertEquals(OrderStatus.PENDING, result.getStatus());
        verify(orderPersistencePort).saveOrder(argThat(order ->
                order.getStatus() == OrderStatus.PENDING
        ));
    }
}