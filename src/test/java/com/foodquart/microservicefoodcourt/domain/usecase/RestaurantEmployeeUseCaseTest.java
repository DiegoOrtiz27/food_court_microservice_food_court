package com.foodquart.microservicefoodcourt.domain.usecase;

import com.foodquart.microservicefoodcourt.domain.exception.DomainException;
import com.foodquart.microservicefoodcourt.domain.model.RestaurantEmployeeModel;
import com.foodquart.microservicefoodcourt.domain.spi.*;
import com.foodquart.microservicefoodcourt.domain.util.RestaurantMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantEmployeeUseCaseTest {

    @Mock
    private IRestaurantEmployeePersistencePort employeePersistence;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IUserClientPort userClientPort;

    @Mock
    private ISecurityContextPort securityContextPort;

    @InjectMocks
    private RestaurantEmployeeUseCase restaurantEmployeeUseCase;

    private RestaurantEmployeeModel validEmployee;
    private final Long ownerId = 1L;
    private final Long restaurantId = 5L;

    @BeforeEach
    void setup() {
        validEmployee = new RestaurantEmployeeModel();
        validEmployee.setEmployeeId(10L);
        validEmployee.setFirstName("Juan");
        validEmployee.setLastName("Pérez");
        validEmployee.setDocumentId("1234567890");
        validEmployee.setPhone("+573001234567");
        validEmployee.setEmail("juan.perez@example.com");
        validEmployee.setPassword("securePassword123");
    }

    @Nested
    @DisplayName("Add Employee to Restaurant")
    class AddEmployeeToRestaurant {

        @Test
        @DisplayName("Should throw exception when restaurant does not exist")
        void shouldThrowExceptionWhenRestaurantNotFound() {
            when(securityContextPort.getCurrentUserId()).thenReturn(ownerId);
            when(restaurantPersistencePort.existsById(restaurantId)).thenReturn(false);

            DomainException exception = assertThrows(DomainException.class,
                    () -> restaurantEmployeeUseCase.addEmployeeToRestaurant(restaurantId, validEmployee));

            assertEquals(String.format(RestaurantMessages.RESTAURANT_NOT_FOUND, restaurantId),
                    exception.getMessage());
            verify(securityContextPort).getCurrentUserId();
            verify(restaurantPersistencePort).existsById(restaurantId);
            verifyNoMoreInteractions(restaurantPersistencePort);
            verifyNoInteractions(userClientPort, employeePersistence);
        }

        @Test
        @DisplayName("Should throw exception when user is not the owner")
        void shouldThrowInvalidOwnerExceptionWhenUserIsNotOwner() {
            when(securityContextPort.getCurrentUserId()).thenReturn(ownerId);
            when(restaurantPersistencePort.existsById(restaurantId)).thenReturn(true);
            when(restaurantPersistencePort.isOwnerOfRestaurant(ownerId, restaurantId)).thenReturn(false);

            DomainException exception = assertThrows(DomainException.class,
                    () -> restaurantEmployeeUseCase.addEmployeeToRestaurant(restaurantId, validEmployee));

            assertEquals(String.format(RestaurantMessages.OWNER_NOT_ASSOCIATED_TO_RESTAURANT, restaurantId),
                    exception.getMessage());
            verify(securityContextPort).getCurrentUserId();
            verify(restaurantPersistencePort).existsById(restaurantId);
            verify(restaurantPersistencePort).isOwnerOfRestaurant(ownerId, restaurantId);
            verifyNoInteractions(userClientPort, employeePersistence);
        }

        @Test
        @DisplayName("Should create employee successfully when valid data")
        void shouldCreateEmployeeSuccessfullyWhenValidData() {
            RestaurantEmployeeModel createdEmployee = new RestaurantEmployeeModel();
            createdEmployee.setEmployeeId(10L);
            createdEmployee.setFirstName("Juan");
            createdEmployee.setLastName("Pérez");
            createdEmployee.setRestaurantId(restaurantId);

            when(securityContextPort.getCurrentUserId()).thenReturn(ownerId);
            when(restaurantPersistencePort.existsById(restaurantId)).thenReturn(true);
            when(restaurantPersistencePort.isOwnerOfRestaurant(ownerId, restaurantId)).thenReturn(true);
            when(userClientPort.createEmployee(any(RestaurantEmployeeModel.class))).thenReturn(createdEmployee);
            when(employeePersistence.addEmployeeToRestaurant(createdEmployee)).thenReturn(createdEmployee);

            RestaurantEmployeeModel result = restaurantEmployeeUseCase.addEmployeeToRestaurant(restaurantId, validEmployee);

            assertNotNull(result);
            assertEquals(10L, result.getEmployeeId());
            assertEquals(restaurantId, result.getRestaurantId());

            verify(securityContextPort).getCurrentUserId();
            verify(restaurantPersistencePort).existsById(restaurantId);
            verify(restaurantPersistencePort).isOwnerOfRestaurant(ownerId, restaurantId);
            verify(userClientPort).createEmployee(argThat(emp ->
                    emp.getRestaurantId().equals(restaurantId)
            ));
            verify(employeePersistence).addEmployeeToRestaurant(createdEmployee);
        }

        @Test
        @DisplayName("Should set restaurant ID before creating employee")
        void shouldSetRestaurantIdBeforeCreatingEmployee() {
            when(securityContextPort.getCurrentUserId()).thenReturn(ownerId);
            when(restaurantPersistencePort.existsById(restaurantId)).thenReturn(true);
            when(restaurantPersistencePort.isOwnerOfRestaurant(ownerId, restaurantId)).thenReturn(true);
            when(userClientPort.createEmployee(any(RestaurantEmployeeModel.class))).thenReturn(validEmployee);
            when(employeePersistence.addEmployeeToRestaurant(validEmployee)).thenReturn(validEmployee);

            restaurantEmployeeUseCase.addEmployeeToRestaurant(restaurantId, validEmployee);

            verify(userClientPort).createEmployee(argThat(employee ->
                    employee.getRestaurantId().equals(restaurantId)
            ));
        }

        @Test
        @DisplayName("Should pass complete employee data to persistence")
        void shouldPassCompleteEmployeeDataToPersistence() {
            when(securityContextPort.getCurrentUserId()).thenReturn(ownerId);
            when(restaurantPersistencePort.existsById(restaurantId)).thenReturn(true);
            when(restaurantPersistencePort.isOwnerOfRestaurant(ownerId, restaurantId)).thenReturn(true);
            when(userClientPort.createEmployee(any(RestaurantEmployeeModel.class))).thenReturn(validEmployee);
            when(employeePersistence.addEmployeeToRestaurant(validEmployee)).thenReturn(validEmployee);

           restaurantEmployeeUseCase.addEmployeeToRestaurant(restaurantId, validEmployee);

            verify(employeePersistence).addEmployeeToRestaurant(argThat(emp ->
                    emp.getEmployeeId().equals(10L) &&
                            emp.getFirstName().equals("Juan") &&
                            emp.getLastName().equals("Pérez") &&
                            emp.getRestaurantId().equals(restaurantId)
            ));
        }
    }
}