package com.foodquart.microservicefoodcourt.domain.usecase;

import com.foodquart.microservicefoodcourt.domain.exception.DomainException;
import com.foodquart.microservicefoodcourt.domain.model.*;
import com.foodquart.microservicefoodcourt.domain.spi.*;
import com.foodquart.microservicefoodcourt.domain.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.foodquart.microservicefoodcourt.domain.util.DishMessages.DISH_NOT_ACTIVE;
import static com.foodquart.microservicefoodcourt.domain.util.DishMessages.DISH_NOT_FROM_RESTAURANT;
import static com.foodquart.microservicefoodcourt.domain.util.OrderMessages.CUSTOMER_HAS_ACTIVE_ORDER;
import static com.foodquart.microservicefoodcourt.domain.util.OrderMessages.RESTAURANT_ID_REQUIRED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderUseCaseTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @Mock
    private IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort;

    @Mock
    private IUserClientPort userClientPort;

    @Mock
    private IMessagingClientPort messagingClientPort;

    @Mock
    private ITracingClientPort tracingClientPort;

    @Mock
    private ISecurityContextPort securityContextPort;

    @InjectMocks
    private OrderUseCase orderUseCase;

    private OrderModel validOrder;
    private DishModel activeDish;
    private final Long validCustomerId = 1L;
    private final Long validRestaurantId = 5L;
    private final Long validEmployeeId = 20L;
    private final Long validDishId1 = 10L;
    private final Long validOrderId = 100L;

    @BeforeEach
    void setup() {
        activeDish = createDish(validDishId1, validRestaurantId, true);

        OrderItemModel item1 = createOrderItem(2, activeDish);
        OrderItemModel item2 = createOrderItem(1, activeDish);

        validOrder = new OrderModel();
        validOrder.setCustomerId(validCustomerId);
        validOrder.setRestaurantId(validRestaurantId);
        validOrder.setItems(List.of(item1, item2));
        validOrder.setId(validOrderId);
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
        @DisplayName("Should create order successfully")
        void shouldCreateOrderSuccessfully() {
            when(securityContextPort.getCurrentUserId()).thenReturn(validCustomerId);
            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
            when(dishPersistencePort.findById(validDishId1)).thenReturn(Optional.of(activeDish));
            when(orderPersistencePort.hasActiveOrders(validCustomerId, OrderStatus.getActiveStatuses())).thenReturn(false);
            when(orderPersistencePort.saveOrder(any(OrderModel.class))).thenAnswer(invocation -> {
                OrderModel savedOrder = invocation.getArgument(0);
                savedOrder.setId(validOrderId);
                return savedOrder;
            });
            doNothing().when(tracingClientPort).recordOrderStatusChange(any(OrderTraceModel.class));

            OrderModel result = orderUseCase.createOrder(validOrder);

            assertNotNull(result);
            assertEquals(OrderStatus.PENDING, result.getStatus());
            assertNotNull(result.getCreatedAt());
            assertNotNull(result.getUpdatedAt());
            assertEquals(validCustomerId, result.getCustomerId());
            assertEquals(validRestaurantId, result.getRestaurantId());
            assertEquals(2, result.getItems().size());
            verify(securityContextPort).getCurrentUserId();
            verify(restaurantPersistencePort).existsById(validRestaurantId);
            verify(dishPersistencePort, times(2)).findById(validDishId1);
            verify(orderPersistencePort).hasActiveOrders(validCustomerId, OrderStatus.getActiveStatuses());
            verify(orderPersistencePort).saveOrder(any(OrderModel.class));
            verify(tracingClientPort).recordOrderStatusChange(any(OrderTraceModel.class));
        }

        @Test
        @DisplayName("Should throw exception when customer has active order")
        void shouldThrowExceptionWhenCustomerHasActiveOrder() {
            when(securityContextPort.getCurrentUserId()).thenReturn(validCustomerId);
            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
            when(orderPersistencePort.hasActiveOrders(validCustomerId, OrderStatus.getActiveStatuses())).thenReturn(true);

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.createOrder(validOrder));

            assertEquals(CUSTOMER_HAS_ACTIVE_ORDER, exception.getMessage());
            verify(securityContextPort).getCurrentUserId();
            verify(restaurantPersistencePort).existsById(validRestaurantId);
            verify(orderPersistencePort).hasActiveOrders(validCustomerId, OrderStatus.getActiveStatuses());
            verifyNoInteractions(dishPersistencePort, tracingClientPort);
        }

        @Test
        @DisplayName("Should throw exception when dish is not from the same restaurant")
        void shouldThrowExceptionWhenDishNotFromRestaurant() {
            DishModel differentRestaurantDish = createDish(99L, 999L, true);
            OrderModel orderWithDifferentRestaurantDish = new OrderModel();
            orderWithDifferentRestaurantDish.setCustomerId(validCustomerId);
            orderWithDifferentRestaurantDish.setRestaurantId(validRestaurantId);
            orderWithDifferentRestaurantDish.setItems(List.of(createOrderItem(1, differentRestaurantDish)));

            when(securityContextPort.getCurrentUserId()).thenReturn(validCustomerId);
            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
            when(dishPersistencePort.findById(99L)).thenReturn(Optional.of(differentRestaurantDish));
            when(orderPersistencePort.hasActiveOrders(validCustomerId, OrderStatus.getActiveStatuses())).thenReturn(false);

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.createOrder(orderWithDifferentRestaurantDish));

            assertEquals(DISH_NOT_FROM_RESTAURANT, exception.getMessage());
            verify(securityContextPort).getCurrentUserId();
            verify(restaurantPersistencePort).existsById(validRestaurantId);
            verify(dishPersistencePort).findById(99L);
            verify(orderPersistencePort).hasActiveOrders(validCustomerId, OrderStatus.getActiveStatuses());
            verifyNoInteractions(tracingClientPort);
        }

        @Test
        @DisplayName("Should throw exception when a dish in the order is not active")
        void shouldThrowExceptionWhenDishIsNotActive() {
            DishModel inactiveDish = createDish(99L, validRestaurantId, false);
            OrderItemModel itemWithInactiveDish = createOrderItem(1, inactiveDish);
            OrderModel orderWithInactive = new OrderModel();
            orderWithInactive.setCustomerId(validCustomerId);
            orderWithInactive.setRestaurantId(validRestaurantId);
            orderWithInactive.setItems(List.of(itemWithInactiveDish));

            when(securityContextPort.getCurrentUserId()).thenReturn(validCustomerId);
            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
            when(dishPersistencePort.findById(99L)).thenReturn(Optional.of(inactiveDish));
            when(orderPersistencePort.hasActiveOrders(validCustomerId, OrderStatus.getActiveStatuses())).thenReturn(false);

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.createOrder(orderWithInactive));

            assertEquals(String.format(DISH_NOT_ACTIVE, 99L), exception.getMessage());
            verify(securityContextPort).getCurrentUserId();
            verify(restaurantPersistencePort).existsById(validRestaurantId);
            verify(dishPersistencePort).findById(99L);
            verify(orderPersistencePort).hasActiveOrders(validCustomerId, OrderStatus.getActiveStatuses());
            verifyNoInteractions(tracingClientPort);
        }

        @Test
        @DisplayName("Should throw exception when restaurant ID is null")
        void shouldThrowExceptionWhenRestaurantIdIsNull() {
            OrderModel orderWithNullRestaurantId = new OrderModel();
            orderWithNullRestaurantId.setCustomerId(validCustomerId);
            orderWithNullRestaurantId.setRestaurantId(null);
            orderWithNullRestaurantId.setItems(List.of(createOrderItem(1, activeDish)));

            when(securityContextPort.getCurrentUserId()).thenReturn(validCustomerId);

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.createOrder(orderWithNullRestaurantId));

            assertEquals(RESTAURANT_ID_REQUIRED, exception.getMessage());
            verify(securityContextPort).getCurrentUserId();
            verifyNoInteractions(restaurantPersistencePort, dishPersistencePort, orderPersistencePort, tracingClientPort);
        }
    }

    @Nested
    @DisplayName("Get Orders By Restaurant Tests")
    class GetOrdersByRestaurantTests {

        @Test
        @DisplayName("Should return orders Pagination when valid parameters")
        void shouldReturnOrdersPagination() {
            List<OrderModel> orderList = List.of(validOrder);
            Pagination<OrderModel> expectedPagination = new Pagination<>(orderList, 0, 10, 1); // Ajusta el total según sea necesario

            when(securityContextPort.getCurrentUserId()).thenReturn(validEmployeeId);
            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
            when(restaurantEmployeePersistencePort.existsByEmployeeIdAndRestaurantId(validEmployeeId, validRestaurantId)).thenReturn(true);
            when(orderPersistencePort.findByRestaurantIdAndStatus(validRestaurantId, OrderStatus.PENDING, 0, 10)).thenReturn(expectedPagination);

            Pagination<OrderModel> result = orderUseCase.getOrdersByRestaurant(validRestaurantId, OrderStatus.PENDING, 0, 10);

            assertNotNull(result);
            assertEquals(expectedPagination, result);
            verify(orderPersistencePort).findByRestaurantIdAndStatus(validRestaurantId, OrderStatus.PENDING, 0, 10);
        }

        @Test
        @DisplayName("Should throw exception when restaurant doesn't exist")
        void shouldThrowWhenRestaurantNotExist() {
            when(securityContextPort.getCurrentUserId()).thenReturn(validEmployeeId);
            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(false);

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.getOrdersByRestaurant(validRestaurantId, OrderStatus.PENDING, 0, 10));

            assertEquals(String.format(RestaurantMessages.RESTAURANT_NOT_FOUND, validRestaurantId), exception.getMessage());
            verify(restaurantPersistencePort).existsById(validRestaurantId);
            verifyNoInteractions(orderPersistencePort, restaurantEmployeePersistencePort);
        }

        @Test
        @DisplayName("Should throw exception when employee not associated with restaurant")
        void shouldThrowWhenEmployeeNotAssociated() {
            when(securityContextPort.getCurrentUserId()).thenReturn(validEmployeeId);
            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
            when(restaurantEmployeePersistencePort.existsByEmployeeIdAndRestaurantId(validEmployeeId, validRestaurantId)).thenReturn(false);

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.getOrdersByRestaurant(validRestaurantId , OrderStatus.PENDING, 0, 10));

            assertEquals(String.format(RestaurantMessages.EMPLOYEE_NOT_ASSOCIATED_TO_RESTAURANT, validRestaurantId), exception.getMessage());
            verify(restaurantPersistencePort).existsById(validRestaurantId);
            verify(restaurantEmployeePersistencePort).existsByEmployeeIdAndRestaurantId(validEmployeeId, validRestaurantId);
            verifyNoInteractions(orderPersistencePort);
        }
    }

    @Nested
    @DisplayName("Assign Order To Employee Tests")
    class AssignOrderToEmployeeTests {

        private final Long validOrderId = 100L;
        private final Long invalidOrderId = 999L;
        private final Long validEmployeeId = 20L;
        private final Long validRestaurantId = 30L;

        @Test
        @DisplayName("Should throw exception when orderId is null")
        void shouldThrowWhenOrderIdIsNull() {
            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.assignOrderToEmployee(null));

            assertTrue(exception.getMessage().contains(OrderMessages.ORDER_ID_REQUIRED));
        }

        @Test
        @DisplayName("Should throw exception when employeeId is null")
        void shouldThrowWhenEmployeeIdIsNull() {
            when(securityContextPort.getCurrentUserId()).thenReturn(null);
            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.assignOrderToEmployee(validOrderId));

            assertTrue(exception.getMessage().contains(OrderMessages.EMPLOYEE_ID_REQUIRED));
        }

        @Test
        @DisplayName("Should throw exception when order doesn't exist")
        void shouldThrowWhenOrderNotFound() {
            when(orderPersistencePort.findById(invalidOrderId)).thenReturn(Optional.empty());

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.assignOrderToEmployee(invalidOrderId));

            assertEquals(String.format(OrderMessages.ORDER_NOT_FOUND, invalidOrderId), exception.getMessage());
            verify(orderPersistencePort, never()).saveOrder(any());
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
                    () -> orderUseCase.assignOrderToEmployee(validOrderId));

            assertEquals(String.format(RestaurantMessages.RESTAURANT_NOT_FOUND, validRestaurantId), exception.getMessage());
            verify(orderPersistencePort, never()).saveOrder(any());
        }

        @Test
        @DisplayName("Should throw exception when employee doesn't belong to restaurant")
        void shouldThrowWhenEmployeeNotInRestaurant() {
            OrderModel pendingOrder = new OrderModel();
            pendingOrder.setId(validOrderId);
            pendingOrder.setStatus(OrderStatus.PENDING);
            pendingOrder.setRestaurantId(validRestaurantId);

            when(securityContextPort.getCurrentUserId()).thenReturn(validEmployeeId);
            when(orderPersistencePort.findById(validOrderId)).thenReturn(Optional.of(pendingOrder));
            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
            when(restaurantEmployeePersistencePort.existsByEmployeeIdAndRestaurantId(validEmployeeId, validRestaurantId))
                    .thenReturn(false);

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.assignOrderToEmployee(validOrderId));

            assertEquals(String.format(RestaurantMessages.EMPLOYEE_NOT_ASSOCIATED_TO_RESTAURANT, validRestaurantId),
                    exception.getMessage());
            verify(orderPersistencePort, never()).saveOrder(any());
        }


        @Test
        @DisplayName("Should throw exception when order status is CANCELLED")
        void shouldThrowWhenStatusIsCancelled() {
            OrderModel order = new OrderModel();
            order.setId(validOrderId);
            order.setStatus(OrderStatus.CANCELLED);
            order.setRestaurantId(validRestaurantId);

            when(securityContextPort.getCurrentUserId()).thenReturn(validEmployeeId);
            when(orderPersistencePort.findById(validOrderId)).thenReturn(Optional.of(order));
            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
            when(restaurantEmployeePersistencePort.existsByEmployeeIdAndRestaurantId(validEmployeeId, validRestaurantId))
                    .thenReturn(true);

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.assignOrderToEmployee(validOrderId));

            assertEquals(OrderMessages.ORDER_IS_NOT_PENDING, exception.getMessage());
            verify(orderPersistencePort, never()).saveOrder(any());
        }

        @Test
        @DisplayName("Should assign order to employee successfully")
        void shouldAssignOrderToEmployee() {
            OrderModel pendingOrder = new OrderModel();
            pendingOrder.setId(validOrderId);
            pendingOrder.setStatus(OrderStatus.PENDING);
            pendingOrder.setRestaurantId(validRestaurantId);

            when(securityContextPort.getCurrentUserId()).thenReturn(validEmployeeId);
            when(orderPersistencePort.findById(validOrderId)).thenReturn(Optional.of(pendingOrder));
            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
            when(restaurantEmployeePersistencePort.existsByEmployeeIdAndRestaurantId(validEmployeeId, validRestaurantId))
                    .thenReturn(true);
            when(orderPersistencePort.saveOrder(any(OrderModel.class))).thenAnswer(invocation -> invocation.getArgument(0));
            doNothing().when(tracingClientPort).recordOrderStatusChange(any(OrderTraceModel.class)); // Simula la llamada al tracing client

            OrderModel result = orderUseCase.assignOrderToEmployee(validOrderId);

            assertNotNull(result);
            assertEquals(OrderStatus.IN_PREPARATION, result.getStatus());
            assertEquals(validEmployeeId, result.getAssignedEmployeeId());
            verify(orderPersistencePort).saveOrder(pendingOrder);
            verify(tracingClientPort).recordOrderStatusChange(any(OrderTraceModel.class)); // Verifica la llamada al tracing client
        }
    }

    @Nested
    @DisplayName("Notify Order Ready Tests")
    class NotifyOrderReadyTests {
        private final Long validOrderId = 100L;
        private final Long invalidOrderId = 999L;
        private final Long validEmployeeId = 20L;
        private final String validPhone = "+573001234567";


        @Test
        @DisplayName("Should notify order ready successfully")
        void shouldNotifyOrderReadySuccessfully() {
            OrderModel inPreparationOrder = new OrderModel();
            inPreparationOrder.setId(validOrderId);
            inPreparationOrder.setStatus(OrderStatus.IN_PREPARATION);
            inPreparationOrder.setRestaurantId(validRestaurantId);
            inPreparationOrder.setCustomerId(validCustomerId);
            inPreparationOrder.setAssignedEmployeeId(validEmployeeId);

            CustomerModel customer = new CustomerModel();
            customer.setPhone(validPhone);

            when(securityContextPort.getCurrentUserId()).thenReturn(validEmployeeId);
            when(orderPersistencePort.findById(validOrderId)).thenReturn(Optional.of(inPreparationOrder));
            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
            when(restaurantEmployeePersistencePort.existsByEmployeeIdAndRestaurantId(validEmployeeId, validRestaurantId)).thenReturn(true);
            when(orderPersistencePort.hasAssignedOrder(validEmployeeId, validOrderId)).thenReturn(true);
            when(userClientPort.getUserInfo(validCustomerId)).thenReturn(customer);
            when(messagingClientPort.notifyOrderReady(any())).thenReturn(true);
            when(orderPersistencePort.saveOrder(any())).thenAnswer(invocation -> invocation.getArgument(0));
            doNothing().when(tracingClientPort).recordOrderStatusChange(any(OrderTraceModel.class)); // Simula la llamada al tracing client

            OrderModel result = orderUseCase.notifyOrderReady(validOrderId);

            assertNotNull(result);
            assertEquals(OrderStatus.READY, result.getStatus());
            assertNotNull(result.getSecurityPin());
            verify(messagingClientPort).notifyOrderReady(any());
            verify(orderPersistencePort).saveOrder(any());
            verify(tracingClientPort).recordOrderStatusChange(any(OrderTraceModel.class)); // Verifica la llamada al tracing client
        }

        @Test
        @DisplayName("Should throw exception when order not found")
        void shouldThrowWhenOrderNotFound() {
            when(orderPersistencePort.findById(invalidOrderId)).thenReturn(Optional.empty());

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.notifyOrderReady(invalidOrderId));

            assertEquals(String.format(OrderMessages.ORDER_NOT_FOUND, invalidOrderId), exception.getMessage());
            verify(messagingClientPort, never()).notifyOrderReady(any());
        }

        @Test
        @DisplayName("Should throw exception when order is not in preparation")
        void shouldThrowWhenOrderNotInPreparation() {
            OrderModel pendingOrder = new OrderModel();
            pendingOrder.setId(validOrderId);
            pendingOrder.setStatus(OrderStatus.PENDING);
            pendingOrder.setRestaurantId(validRestaurantId);
            pendingOrder.setAssignedEmployeeId(validEmployeeId);

            when(securityContextPort.getCurrentUserId()).thenReturn(validEmployeeId);
            when(orderPersistencePort.findById(validOrderId)).thenReturn(Optional.of(pendingOrder));
            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
            when(restaurantEmployeePersistencePort.existsByEmployeeIdAndRestaurantId(validEmployeeId, validRestaurantId)).thenReturn(true);

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.notifyOrderReady(validOrderId));

            assertEquals(OrderMessages.ORDER_IS_NOT_PREPARING, exception.getMessage());
            verify(messagingClientPort, never()).notifyOrderReady(any());
        }

        @Test
        @DisplayName("Should throw exception when employee not associated with restaurant")
        void shouldThrowWhenEmployeeNotAssociated() {
            OrderModel inPreparationOrder = new OrderModel();
            inPreparationOrder.setId(validOrderId);
            inPreparationOrder.setStatus(OrderStatus.IN_PREPARATION);
            inPreparationOrder.setRestaurantId(validRestaurantId);

            when(securityContextPort.getCurrentUserId()).thenReturn(validEmployeeId);
            when(orderPersistencePort.findById(validOrderId)).thenReturn(Optional.of(inPreparationOrder));
            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
            when(restaurantEmployeePersistencePort.existsByEmployeeIdAndRestaurantId(validEmployeeId, validRestaurantId)).thenReturn(false);

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.notifyOrderReady(validOrderId));

            assertEquals(String.format(RestaurantMessages.EMPLOYEE_NOT_ASSOCIATED_TO_RESTAURANT, validRestaurantId), exception.getMessage());
            verify(messagingClientPort, never()).notifyOrderReady(any());
        }

        @Test
        @DisplayName("Should throw exception when employee not assigned to order")
        void shouldThrowWhenEmployeeNotAssignedToOrder() {
            OrderModel inPreparationOrder = new OrderModel();
            inPreparationOrder.setId(validOrderId);
            inPreparationOrder.setStatus(OrderStatus.IN_PREPARATION);
            inPreparationOrder.setRestaurantId(validRestaurantId);
            inPreparationOrder.setAssignedEmployeeId(validEmployeeId);

            when(securityContextPort.getCurrentUserId()).thenReturn(validEmployeeId);
            when(orderPersistencePort.findById(validOrderId)).thenReturn(Optional.of(inPreparationOrder));
            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
            when(restaurantEmployeePersistencePort.existsByEmployeeIdAndRestaurantId(validEmployeeId, validRestaurantId)).thenReturn(true);
            when(orderPersistencePort.hasAssignedOrder(validEmployeeId, validOrderId)).thenReturn(false);

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.notifyOrderReady(validOrderId));

            assertEquals(String.format(OrderMessages.EMPLOYEE_WITH_NOT_ORDER_ASSOCIATED, validEmployeeId, validOrderId), exception.getMessage());
            verify(messagingClientPort, never()).notifyOrderReady(any());
        }

        @Test
        @DisplayName("Should throw exception when notification fails")
        void shouldThrowWhenNotificationFails() {
            OrderModel inPreparationOrder = new OrderModel();
            inPreparationOrder.setId(validOrderId);
            inPreparationOrder.setStatus(OrderStatus.IN_PREPARATION);
            inPreparationOrder.setRestaurantId(validRestaurantId);
            inPreparationOrder.setCustomerId(validCustomerId);
            inPreparationOrder.setAssignedEmployeeId(validEmployeeId);

            CustomerModel customer = new CustomerModel();
            customer.setPhone(validPhone);

            when(securityContextPort.getCurrentUserId()).thenReturn(validEmployeeId);
            when(orderPersistencePort.findById(validOrderId)).thenReturn(Optional.of(inPreparationOrder));
            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
            when(restaurantEmployeePersistencePort.existsByEmployeeIdAndRestaurantId(validEmployeeId, validRestaurantId)).thenReturn(true);
            when(orderPersistencePort.hasAssignedOrder(validEmployeeId, validOrderId)).thenReturn(true);
            when(userClientPort.getUserInfo(validCustomerId)).thenReturn(customer);
            when(messagingClientPort.notifyOrderReady(any())).thenReturn(false);

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.notifyOrderReady(validOrderId));

            assertEquals(OrderMessages.NOTIFICATION_NOT_SENT, exception.getMessage());
            verify(orderPersistencePort, never()).saveOrder(any());
        }
    }

    @Nested
    @DisplayName("Mark Order As Delivered Tests")
    class MarkOrderAsDeliveredTests {
        private final Long validOrderId = 100L;
        private final Long invalidOrderId = 999L;
        private final Long validEmployeeId = 20L;
        private final String validSecurityPin = "1234";
        private final String invalidSecurityPin = "0000";

        @Test
        @DisplayName("Should mark order as delivered successfully with valid data")
        void shouldMarkOrderAsDeliveredSuccessfully() {
            OrderModel readyOrder = new OrderModel();
            readyOrder.setId(validOrderId);
            readyOrder.setStatus(OrderStatus.READY);
            readyOrder.setRestaurantId(validRestaurantId);
            readyOrder.setSecurityPin(validSecurityPin);
            readyOrder.setAssignedEmployeeId(validEmployeeId);

            when(securityContextPort.getCurrentUserId()).thenReturn(validEmployeeId);
            when(orderPersistencePort.findById(validOrderId)).thenReturn(Optional.of(readyOrder));
            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
            when(restaurantEmployeePersistencePort.existsByEmployeeIdAndRestaurantId(validEmployeeId, validRestaurantId)).thenReturn(true);
            when(orderPersistencePort.saveOrder(any(OrderModel.class))).thenAnswer(invocation -> invocation.getArgument(0));
            doNothing().when(tracingClientPort).recordOrderStatusChange(any(OrderTraceModel.class)); // Simula la llamada al tracing client

            OrderModel result = orderUseCase.markOrderAsDelivered(validOrderId, validSecurityPin);

            assertNotNull(result);
            assertEquals(OrderStatus.DELIVERED, result.getStatus());
            verify(orderPersistencePort).saveOrder(readyOrder);
            verify(tracingClientPort).recordOrderStatusChange(any(OrderTraceModel.class)); // Verifica la llamada al tracing client
        }

        @Test
        @DisplayName("Should throw exception when order not found")
        void shouldThrowWhenOrderNotFound() {
            when(orderPersistencePort.findById(invalidOrderId)).thenReturn(Optional.empty());

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.markOrderAsDelivered(invalidOrderId, validSecurityPin));

            assertEquals(String.format(OrderMessages.ORDER_NOT_FOUND, invalidOrderId), exception.getMessage());
            verify(orderPersistencePort, never()).saveOrder(any());
        }

        @Test
        @DisplayName("Should throw exception when order is not in READY status")
        void shouldThrowWhenOrderNotReady() {
            OrderModel inPreparationOrder = new OrderModel();
            inPreparationOrder.setId(validOrderId);
            inPreparationOrder.setStatus(OrderStatus.IN_PREPARATION);
            inPreparationOrder.setRestaurantId(validRestaurantId);

            when(securityContextPort.getCurrentUserId()).thenReturn(validEmployeeId);
            when(orderPersistencePort.findById(validOrderId)).thenReturn(Optional.of(inPreparationOrder));
            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
            when(restaurantEmployeePersistencePort.existsByEmployeeIdAndRestaurantId(validEmployeeId, validRestaurantId)).thenReturn(true);

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.markOrderAsDelivered(validOrderId, validSecurityPin));

            assertEquals(OrderMessages.ORDER_NOT_READY_FOR_DELIVERY, exception.getMessage());
            verify(orderPersistencePort, never()).saveOrder(any());
        }

        @Test
        @DisplayName("Should throw exception when restaurant doesn't exist")
        void shouldThrowWhenRestaurantNotFound() {
            OrderModel readyOrder = new OrderModel();
            readyOrder.setId(validOrderId);
            readyOrder.setStatus(OrderStatus.READY);
            readyOrder.setRestaurantId(validRestaurantId);
            readyOrder.setSecurityPin(validSecurityPin);

            when(orderPersistencePort.findById(validOrderId)).thenReturn(Optional.of(readyOrder));
            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(false);

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.markOrderAsDelivered(validOrderId, validSecurityPin));

            assertEquals(String.format(RestaurantMessages.RESTAURANT_NOT_FOUND, validRestaurantId), exception.getMessage());
            verify(orderPersistencePort, never()).saveOrder(any());
        }

        @Test
        @DisplayName("Should throw exception when employee not associated with restaurant")
        void shouldThrowWhenEmployeeNotAssociated() {
            OrderModel readyOrder = new OrderModel();
            readyOrder.setId(validOrderId);
            readyOrder.setStatus(OrderStatus.READY);
            readyOrder.setRestaurantId(validRestaurantId);
            readyOrder.setSecurityPin(validSecurityPin);

            when(securityContextPort.getCurrentUserId()).thenReturn(validEmployeeId);
            when(orderPersistencePort.findById(validOrderId)).thenReturn(Optional.of(readyOrder));
            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
            when(restaurantEmployeePersistencePort.existsByEmployeeIdAndRestaurantId(validEmployeeId, validRestaurantId)).thenReturn(false);

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.markOrderAsDelivered(validOrderId, validSecurityPin));

            assertEquals(String.format(RestaurantMessages.EMPLOYEE_NOT_ASSOCIATED_TO_RESTAURANT, validRestaurantId), exception.getMessage());
            verify(orderPersistencePort, never()).saveOrder(any());
        }

        @Test
        @DisplayName("Should throw exception when security pin is invalid")
        void shouldThrowWhenSecurityPinInvalid() {
            OrderModel readyOrder = new OrderModel();
            readyOrder.setId(validOrderId);
            readyOrder.setStatus(OrderStatus.READY);
            readyOrder.setRestaurantId(validRestaurantId);
            readyOrder.setSecurityPin(validSecurityPin);

            when(securityContextPort.getCurrentUserId()).thenReturn(validEmployeeId);
            when(orderPersistencePort.findById(validOrderId)).thenReturn(Optional.of(readyOrder));
            when(restaurantPersistencePort.existsById(validRestaurantId)).thenReturn(true);
            when(restaurantEmployeePersistencePort.existsByEmployeeIdAndRestaurantId(validEmployeeId, validRestaurantId)).thenReturn(true);

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.markOrderAsDelivered(validOrderId, invalidSecurityPin));

            assertEquals(OrderMessages.INVALID_SECURITY_PIN, exception.getMessage());
            verify(orderPersistencePort, never()).saveOrder(any());
        }

        @Test
        @DisplayName("Should throw exception when security pin is null")
        void shouldThrowWhenSecurityPinNull() {
            OrderModel readyOrder = new OrderModel();
            readyOrder.setSecurityPin(validSecurityPin);
            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.markOrderAsDelivered(validOrderId, null));

            assertEquals(OrderMessages.SECURITY_PIN_REQUIRED, exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when security pin is empty")
        void shouldThrowWhenSecurityPinEmpty() {
            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.markOrderAsDelivered(validOrderId, ""));

            assertEquals(OrderMessages.SECURITY_PIN_REQUIRED, exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Cancel Order Tests")
    class CancelOrderTests {
        private final Long validOrderId = 100L;
        private final Long invalidOrderId = 999L;
        private final Long validCustomerId = 1L;

        @Test
        @DisplayName("Should cancel order successfully when in PENDING status and belongs to customer")
        void shouldCancelOrderSuccessfully() {
            OrderModel pendingOrder = new OrderModel();
            pendingOrder.setId(validOrderId);
            pendingOrder.setStatus(OrderStatus.PENDING);
            pendingOrder.setCustomerId(validCustomerId);
            pendingOrder.setRestaurantId(validRestaurantId);

            when(securityContextPort.getCurrentUserId()).thenReturn(validCustomerId);
            when(orderPersistencePort.findById(validOrderId)).thenReturn(Optional.of(pendingOrder));
            when(orderPersistencePort.saveOrder(any(OrderModel.class))).thenAnswer(invocation -> invocation.getArgument(0));
            doNothing().when(tracingClientPort).recordOrderStatusChange(any(OrderTraceModel.class));

            OrderModel result = orderUseCase.cancelOrder(validOrderId);

            assertNotNull(result);
            assertEquals(OrderStatus.CANCELLED, result.getStatus());
            verify(orderPersistencePort).saveOrder(pendingOrder);
            verify(tracingClientPort).recordOrderStatusChange(any(OrderTraceModel.class));
        }

        @Test
        @DisplayName("Should throw exception when orderId is null")
        void shouldThrowWhenOrderIdIsNull() {
            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.cancelOrder(null));

            assertEquals(OrderMessages.ORDER_ID_REQUIRED, exception.getMessage());
            verify(orderPersistencePort, never()).saveOrder(any());
        }

        @Test
        @DisplayName("Should throw exception when order not found")
        void shouldThrowWhenOrderNotFound() {
            when(orderPersistencePort.findById(invalidOrderId)).thenReturn(Optional.empty());

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.cancelOrder(invalidOrderId));

            assertEquals(String.format(OrderMessages.ORDER_NOT_FOUND, invalidOrderId), exception.getMessage());
            verify(orderPersistencePort, never()).saveOrder(any());
        }

        @Test
        @DisplayName("Should throw exception when order does not belong to customer")
        void shouldThrowWhenOrderNotBelongToCustomer() {
            OrderModel pendingOrder = new OrderModel();
            pendingOrder.setId(validOrderId);
            pendingOrder.setStatus(OrderStatus.PENDING);
            Long otherCustomerId = 2L;
            pendingOrder.setCustomerId(otherCustomerId);

            when(orderPersistencePort.findById(validOrderId)).thenReturn(Optional.of(pendingOrder));

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.cancelOrder(validOrderId));

            assertEquals(OrderMessages.ORDER_NOT_BELONG_TO_CUSTOMER, exception.getMessage());
            verify(orderPersistencePort, never()).saveOrder(any());
        }

        @Test
        @DisplayName("Should throw exception when order is not in PENDING status")
        void shouldThrowWhenOrderNotPending() {
            OrderModel inPreparationOrder = new OrderModel();
            inPreparationOrder.setId(validOrderId);
            inPreparationOrder.setStatus(OrderStatus.IN_PREPARATION);
            inPreparationOrder.setCustomerId(validCustomerId);

            when(securityContextPort.getCurrentUserId()).thenReturn(validCustomerId);
            when(orderPersistencePort.findById(validOrderId)).thenReturn(Optional.of(inPreparationOrder));

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.cancelOrder(validOrderId));

            assertEquals(OrderMessages.ORDER_IS_NOT_PENDING, exception.getMessage());
            verify(orderPersistencePort, never()).saveOrder(any());
        }

        @Test
        @DisplayName("Should not update order when status is already CANCELLED")
        void shouldNotUpdateWhenAlreadyCancelled() {
            OrderModel cancelledOrder = new OrderModel();
            cancelledOrder.setId(validOrderId);
            cancelledOrder.setStatus(OrderStatus.CANCELLED);
            cancelledOrder.setCustomerId(validCustomerId);

            when(securityContextPort.getCurrentUserId()).thenReturn(validCustomerId);
            when(orderPersistencePort.findById(validOrderId)).thenReturn(Optional.of(cancelledOrder));

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderUseCase.cancelOrder(validOrderId));

            assertEquals(OrderMessages.ORDER_IS_NOT_PENDING, exception.getMessage());
            verify(orderPersistencePort, never()).saveOrder(any());
        }
    }
}