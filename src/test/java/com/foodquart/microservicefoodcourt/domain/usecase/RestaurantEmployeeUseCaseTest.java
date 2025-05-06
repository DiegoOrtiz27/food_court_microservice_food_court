package com.foodquart.microservicefoodcourt.domain.usecase;

import com.foodquart.microservicefoodcourt.domain.exception.InvalidOwnerException;
import com.foodquart.microservicefoodcourt.domain.model.RestaurantEmployeeModel;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantEmployeePersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IUserClientPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    @InjectMocks
    private RestaurantEmployeeUseCase restaurantEmployeeUseCase;

    private RestaurantEmployeeModel validEmployee;
    private final Long validOwnerId = 1L;
    private final Long validRestaurantId = 5L;

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
        validEmployee.setRestaurantId(validRestaurantId);
    }


    @Test
    @DisplayName("Should throw InvalidOwnerException when user is not the owner")
    void shouldThrowInvalidOwnerExceptionWhenUserIsNotOwner() {
        when(restaurantPersistencePort.isOwnerOfRestaurant(validOwnerId, validRestaurantId))
                .thenReturn(false);

        InvalidOwnerException exception = assertThrows(InvalidOwnerException.class,
                () -> restaurantEmployeeUseCase.addEmployeeToRestaurant(validOwnerId, validEmployee));

        assertEquals("User is not the owner of restaurant with ID 5", exception.getMessage());
        verify(restaurantPersistencePort).isOwnerOfRestaurant(validOwnerId, validRestaurantId);
        verifyNoInteractions(userClientPort, employeePersistence);
    }

    @Test
    @DisplayName("Should create employee successfully when user is owner")
    void shouldCreateEmployeeSuccessfullyWhenUserIsOwner() {
        RestaurantEmployeeModel createdEmployee = new RestaurantEmployeeModel();
        createdEmployee.setEmployeeId(10L);
        createdEmployee.setFirstName("Juan");
        createdEmployee.setLastName("Pérez");
        createdEmployee.setRestaurantId(validRestaurantId);

        when(restaurantPersistencePort.isOwnerOfRestaurant(validOwnerId, validRestaurantId))
                .thenReturn(true);
        when(userClientPort.createEmployee(validEmployee)).thenReturn(createdEmployee);
        when(employeePersistence.addEmployeeToRestaurant(createdEmployee)).thenReturn(createdEmployee);

        RestaurantEmployeeModel result = restaurantEmployeeUseCase.addEmployeeToRestaurant(validOwnerId, validEmployee);

        assertNotNull(result);
        assertEquals(10L, result.getEmployeeId());
        assertEquals("Juan", result.getFirstName());
        assertEquals("Pérez", result.getLastName());
        assertEquals(validRestaurantId, result.getRestaurantId());

        verify(restaurantPersistencePort).isOwnerOfRestaurant(validOwnerId, validRestaurantId);
        verify(userClientPort).createEmployee(validEmployee);
        verify(employeePersistence).addEmployeeToRestaurant(createdEmployee);
    }

    @Test
    @DisplayName("Should pass complete employee data to user client")
    void shouldPassCompleteEmployeeDataToUserClient() {
        when(restaurantPersistencePort.isOwnerOfRestaurant(validOwnerId, validRestaurantId))
                .thenReturn(true);
        when(userClientPort.createEmployee(validEmployee)).thenReturn(validEmployee);
        when(employeePersistence.addEmployeeToRestaurant(validEmployee)).thenReturn(validEmployee);

        restaurantEmployeeUseCase.addEmployeeToRestaurant(validOwnerId, validEmployee);

        verify(userClientPort).createEmployee(argThat(employee ->
                employee.getFirstName().equals("Juan") &&
                        employee.getLastName().equals("Pérez") &&
                        employee.getDocumentId().equals("1234567890") &&
                        employee.getPhone().equals("+573001234567") &&
                        employee.getEmail().equals("juan.perez@example.com") &&
                        employee.getPassword().equals("securePassword123") &&
                        employee.getRestaurantId().equals(validRestaurantId)
        ));
    }

    @Test
    @DisplayName("Should return the employee with all fields populated")
    void shouldReturnEmployeeWithAllFieldsPopulated() {
        when(restaurantPersistencePort.isOwnerOfRestaurant(validOwnerId, validRestaurantId))
                .thenReturn(true);
        when(userClientPort.createEmployee(validEmployee)).thenReturn(validEmployee);
        when(employeePersistence.addEmployeeToRestaurant(validEmployee)).thenReturn(validEmployee);

        RestaurantEmployeeModel result = restaurantEmployeeUseCase.addEmployeeToRestaurant(validOwnerId, validEmployee);

        assertAll(
                () -> assertEquals(10L, result.getEmployeeId()),
                () -> assertEquals("Juan", result.getFirstName()),
                () -> assertEquals("Pérez", result.getLastName()),
                () -> assertEquals("1234567890", result.getDocumentId()),
                () -> assertEquals("+573001234567", result.getPhone()),
                () -> assertEquals("juan.perez@example.com", result.getEmail()),
                () -> assertEquals("securePassword123", result.getPassword()),
                () -> assertEquals(validRestaurantId, result.getRestaurantId())
        );
    }
}