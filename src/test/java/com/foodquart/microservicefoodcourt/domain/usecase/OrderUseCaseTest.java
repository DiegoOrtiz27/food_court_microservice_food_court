package com.foodquart.microservicefoodcourt.domain.usecase;

import com.foodquart.microservicefoodcourt.domain.exception.DomainException;
import com.foodquart.microservicefoodcourt.domain.model.*;
import com.foodquart.microservicefoodcourt.domain.spi.*;
import com.foodquart.microservicefoodcourt.domain.util.OrderMessages;
import com.foodquart.microservicefoodcourt.domain.util.OrderStatus;
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
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

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

    @Mock
    private IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort;

    @InjectMocks
    private OrderUseCase orderUseCase;

    private OrderModel validOrder;
    private final Long validCustomerId = 1L;
    private final Long validRestaurantId = 5L;
    private final Long validDishId1 = 10L;
    private final Long validDishId2 = 11L;
    private final Long validEmployeeId = 20L;

    @BeforeEach
    void setup() {
        DishModel dish1 = createDish(validDishId1, validRestaurantId, true);
        DishModel dish2 = createDish(validDishId2, validRestaurantId, true);

        OrderItemModel item1 = createOrderItem(2, dish1);
        OrderItemModel item2 = createOrderItem(1, dish2);

        validOrder = new OrderModel();
        validOrder.setCustomerId(validCustomerId);
        validOrder.setRestaurantId(validRestaurantId);
        validOrder.setItems(List.of(item1, item2));
    }

    private DishModel createDish(Long id, Long restaurantId, Boolean active) {
        DishModel dish = new DishModel();
        dish.setId(id);
        dish.setRestaurantId(restaurantId);
        dish.setActive(active);
        return dish;
    }

    private OrderItemModel createOrderItem(int quantity, DishModel dish) {
        OrderItemModel item = new OrderItemModel();
        item.setQuantity(quantity);
        item.setDish(dish);
        return item;
    }

    @Nested
    @DisplayName("Create Order Tests")
    class CreateOrderTests {

        @Test
        @DisplayName("Should create order successfully with valid data")
        void shouldCreateOrderSuccessfully() {
            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
            when(orderPersistencePort.hasActiveOrders(validCustomerId, OrderStatus.getActiveStatuses())).thenReturn(false);
            when(dishPersistencePort.findById(validDishId1)).thenReturn(java.util.Optional.of(validOrder.getItems().get(0).getDish()));
            when(dishPersistencePort.findById(validDishId2)).thenReturn(java.util.Optional.of(validOrder.getItems().get(1).getDish()));
            when(orderPersistencePort.saveOrder(any(OrderModel.class))).thenReturn(validOrder);

            OrderModel result = orderUseCase.createOrder(validOrder);

            assertNotNull(result);
            assertEquals(OrderStatus.PENDING, result.getStatus());
            verify(orderPersistencePort).saveOrder(any(OrderModel.class));
        }

        @Test
        @DisplayName("Should throw exception when restaurant doesn't exist")
        void shouldThrowWhenRestaurantNotExist() {
            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(false);

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.createOrder(validOrder));

            assertEquals("Restaurant with id '5' not found", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when customer has active orders")
        void shouldThrowWhenCustomerHasActiveOrders() {
            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
            when(orderPersistencePort.hasActiveOrders(validCustomerId, OrderStatus.getActiveStatuses())).thenReturn(true);

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.createOrder(validOrder));

            assertEquals("Customer has an active order in progress", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when dish doesn't exist")
        void shouldThrowWhenDishNotExist() {
            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
            when(orderPersistencePort.hasActiveOrders(validCustomerId, OrderStatus.getActiveStatuses())).thenReturn(false);
            when(dishPersistencePort.findById(validDishId1)).thenReturn(java.util.Optional.empty());

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.createOrder(validOrder));

            assertEquals("Dish with id '10' not found", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when dish is not active")
        void shouldThrowWhenDishNotActive() {
            DishModel inactiveDish = createDish(validDishId1, validRestaurantId, false);
            validOrder.getItems().getFirst().setDish(inactiveDish);

            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
            when(orderPersistencePort.hasActiveOrders(validCustomerId, OrderStatus.getActiveStatuses())).thenReturn(false);
            when(dishPersistencePort.findById(validDishId1)).thenReturn(java.util.Optional.of(inactiveDish));

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.createOrder(validOrder));

            assertEquals("Dish with id '10' is not active", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when dish doesn't belong to restaurant")
        void shouldThrowWhenDishNotFromRestaurant() {
            DishModel wrongRestaurantDish = createDish(validDishId1, 99L, true);
            validOrder.getItems().getFirst().setDish(wrongRestaurantDish);

            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
            when(orderPersistencePort.hasActiveOrders(validCustomerId, OrderStatus.getActiveStatuses())).thenReturn(false);
            when(dishPersistencePort.findById(validDishId1)).thenReturn(java.util.Optional.of(wrongRestaurantDish));

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.createOrder(validOrder));

            assertEquals("Dish does not belong to the restaurant", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Get Orders By Restaurant Tests")
    class GetOrdersByRestaurantTests {

        @Test
        @DisplayName("Should return orders page when valid parameters")
        void shouldReturnOrdersPage() {
            PageRequest pageable = PageRequest.of(0, 10);
            Page<OrderModel> expectedPage = new PageImpl<>(List.of(validOrder), pageable, 1);

            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
            when(restaurantEmployeePersistencePort.existsByEmployeeIdAndRestaurantId(validEmployeeId, validRestaurantId)).thenReturn(true);
            when(orderPersistencePort.findByRestaurantIdAndStatus(validRestaurantId, OrderStatus.PENDING, 0, 10)).thenReturn(expectedPage);

            Page<OrderModel> result = orderUseCase.getOrdersByRestaurant(validEmployeeId, validRestaurantId, OrderStatus.PENDING, 0, 10);

            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            verify(orderPersistencePort).findByRestaurantIdAndStatus(validRestaurantId, OrderStatus.PENDING, 0, 10);
        }

        @Test
        @DisplayName("Should throw exception when restaurant doesn't exist")
        void shouldThrowWhenRestaurantNotExist() {
            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(false);

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.getOrdersByRestaurant(validEmployeeId, validRestaurantId, OrderStatus.PENDING, 0, 10));

            assertEquals("Restaurant with id '5' not found", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when employee not associated with restaurant")
        void shouldThrowWhenEmployeeNotAssociated() {
            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
            when(restaurantEmployeePersistencePort.existsByEmployeeIdAndRestaurantId(validEmployeeId, validRestaurantId)).thenReturn(false);

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.getOrdersByRestaurant(validEmployeeId, validRestaurantId, OrderStatus.PENDING, 0, 10));

            assertEquals("Employee is not associated with restaurant with id '5'", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Assign Order To Employee Tests")
    class AssignOrderToEmployeeTests {

        private final Long validOrderId = 100L;
        private final Long invalidOrderId = 999L;
        private final Long validEmployeeId = 20L;

        @Test
        @DisplayName("Should assign order to employee successfully")
        void shouldAssignOrderToEmployee() {
            OrderModel pendingOrder = new OrderModel();
            pendingOrder.setId(validOrderId);
            pendingOrder.setStatus(OrderStatus.PENDING);
            pendingOrder.setRestaurantId(validRestaurantId);

            when(orderPersistencePort.findById(validOrderId)).thenReturn(Optional.of(pendingOrder));
            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
            when(orderPersistencePort.updateOrder(any(OrderModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

            OrderModel result = orderUseCase.assignOrderToEmployee(validOrderId, validEmployeeId);

            assertNotNull(result);
            assertEquals(OrderStatus.IN_PREPARATION, result.getStatus());
            assertEquals(validEmployeeId, result.getAssignedEmployeeId());
            verify(orderPersistencePort).updateOrder(pendingOrder);
        }

        @Test
        @DisplayName("Should throw exception when order doesn't exist")
        void shouldThrowWhenOrderNotFound() {
            when(orderPersistencePort.findById(invalidOrderId)).thenReturn(Optional.empty());

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.assignOrderToEmployee(invalidOrderId, validEmployeeId));

            assertEquals(String.format(OrderMessages.ORDER_NOT_FOUND, invalidOrderId), exception.getMessage());
            verify(orderPersistencePort, never()).updateOrder(any());
        }

        @Test
        @DisplayName("Should throw exception when order is already in preparation")
        void shouldThrowWhenOrderAlreadyAssigned() {
            OrderModel inPreparationOrder = new OrderModel();
            inPreparationOrder.setId(validOrderId);
            inPreparationOrder.setStatus(OrderStatus.IN_PREPARATION);

            when(orderPersistencePort.findById(validOrderId)).thenReturn(Optional.of(inPreparationOrder));

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.assignOrderToEmployee(validOrderId, validEmployeeId));

            assertEquals(OrderMessages.ORDER_ALREADY_ASSIGNED, exception.getMessage());
            verify(orderPersistencePort, never()).updateOrder(any());
            verify(restaurantPersistencePort, never()).existsById(any());
        }

        @Test
        @DisplayName("Should throw exception when restaurant doesn't exist")
        void shouldThrowWhenRestaurantNotFound() {
            OrderModel pendingOrder = new OrderModel();
            pendingOrder.setId(validOrderId);
            pendingOrder.setStatus(OrderStatus.PENDING);
            pendingOrder.setRestaurantId(validRestaurantId);

            when(orderPersistencePort.findById(validOrderId)).thenReturn(Optional.of(pendingOrder));
            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(false);

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.assignOrderToEmployee(validOrderId, validEmployeeId));

            assertEquals(String.format(RestaurantMessages.RESTAURANT_NOT_FOUND, validRestaurantId), exception.getMessage());
            verify(orderPersistencePort, never()).updateOrder(any());
        }

        @Test
        @DisplayName("Should throw exception when trying to assign a delivered order")
        void shouldThrowWhenOrderIsDelivered() {
            OrderModel deliveredOrder = new OrderModel();
            deliveredOrder.setId(validOrderId);
            deliveredOrder.setStatus(OrderStatus.DELIVERED);

            when(orderPersistencePort.findById(validOrderId)).thenReturn(Optional.of(deliveredOrder));

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.assignOrderToEmployee(validOrderId, validEmployeeId));

            assertEquals(OrderMessages.ORDER_ALREADY_ASSIGNED, exception.getMessage());
            verify(orderPersistencePort, never()).updateOrder(any());
            verify(restaurantPersistencePort, never()).existsById(any());
        }

        @Test
        @DisplayName("Should throw exception when trying to assign a cancelled order")
        void shouldThrowWhenOrderIsCancelled() {
            OrderModel cancelledOrder = new OrderModel();
            cancelledOrder.setId(validOrderId);
            cancelledOrder.setStatus(OrderStatus.CANCELLED);

            when(orderPersistencePort.findById(validOrderId)).thenReturn(Optional.of(cancelledOrder));

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.assignOrderToEmployee(validOrderId, validEmployeeId));

            assertEquals(OrderMessages.ORDER_ALREADY_ASSIGNED, exception.getMessage());
            verify(orderPersistencePort, never()).updateOrder(any());
            verify(restaurantPersistencePort, never()).existsById(any());
        }
    }
}