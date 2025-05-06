package com.foodquart.microservicefoodcourt.infrastructure.configuration;

import com.foodquart.microservicefoodcourt.domain.api.IDishServicePort;
import com.foodquart.microservicefoodcourt.domain.api.IRestaurantEmployeeServicePort;
import com.foodquart.microservicefoodcourt.domain.api.IRestaurantServicePort;
import com.foodquart.microservicefoodcourt.domain.spi.IDishPersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantEmployeePersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IUserClientPort;
import com.foodquart.microservicefoodcourt.domain.usecase.DishUseCase;
import com.foodquart.microservicefoodcourt.domain.usecase.RestaurantEmployeeUseCase;
import com.foodquart.microservicefoodcourt.domain.usecase.RestaurantUseCase;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.IUserFeignClient;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.adapter.UserClientAdapter;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.mapper.IEmployeeRequestMapper;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.adapter.DishJpaAdapter;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.adapter.RestaurantEmployeeJpaAdapter;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.adapter.RestaurantJpaAdapter;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.mapper.IDishEntityMapper;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.mapper.IRestaurantEmployeeEntityMapper;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.repository.IDishRepository;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.repository.IRestaurantEmployeeRepository;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;

    private final IDishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;

    private final IRestaurantEmployeeRepository restaurantEmployeeRepository;
    private final IRestaurantEmployeeEntityMapper restaurantEmployeeEntityMapper;

    private final IUserFeignClient userFeignClient;
    private final IEmployeeRequestMapper employeeRequestMapper;

    @Bean
    public IRestaurantPersistencePort restaurantPersistencePort() {
        return new RestaurantJpaAdapter(
                restaurantRepository,
                restaurantEntityMapper);
    }

    @Bean
    public IRestaurantServicePort restaurantServicePort() {
        return new RestaurantUseCase(
                restaurantPersistencePort());
    }

    @Bean
    public IDishPersistencePort dishPersistencePort() {
        return new DishJpaAdapter(
                dishRepository,
                dishEntityMapper);
    }

    @Bean
    public IDishServicePort dishServicePort() {
        return new DishUseCase(
                dishPersistencePort(),
                restaurantPersistencePort());
    }

    @Bean
    public IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort() {
        return new RestaurantEmployeeJpaAdapter(
                restaurantEmployeeRepository,
                restaurantEmployeeEntityMapper);
    }

    @Bean
    public IRestaurantEmployeeServicePort restaurantEmployeeServicePort() {
        return new RestaurantEmployeeUseCase(
                restaurantEmployeePersistencePort(),
                restaurantPersistencePort(),
                userClientPort());
    }
    @Bean
    public IUserClientPort userClientPort() {
        return new UserClientAdapter(
                userFeignClient,
                employeeRequestMapper);
    }
}
