package com.foodquart.microservicefoodcourt.infrastructure.configuration;

import com.foodquart.microservicefoodcourt.domain.api.IDishServicePort;
import com.foodquart.microservicefoodcourt.domain.api.IOrderServicePort;
import com.foodquart.microservicefoodcourt.domain.api.IRestaurantEmployeeServicePort;
import com.foodquart.microservicefoodcourt.domain.api.IRestaurantServicePort;
import com.foodquart.microservicefoodcourt.domain.spi.*;
import com.foodquart.microservicefoodcourt.domain.usecase.DishUseCase;
import com.foodquart.microservicefoodcourt.domain.usecase.OrderUseCase;
import com.foodquart.microservicefoodcourt.domain.usecase.RestaurantEmployeeUseCase;
import com.foodquart.microservicefoodcourt.domain.usecase.RestaurantUseCase;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.IMessagingFeignClient;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.ITracingFeignClient;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.IUserFeignClient;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.adapter.MessagingClientAdapter;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.adapter.SecurityContextAdapter;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.adapter.TracingClientAdapter;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.adapter.UserClientAdapter;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.mapper.INotificationRequestMapper;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.mapper.IOrderTraceRequestMapper;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.mapper.IUserRequestMapper;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.mapper.IUserResponseMapper;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.adapter.DishJpaAdapter;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.adapter.OrderJpaAdapter;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.adapter.RestaurantEmployeeJpaAdapter;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.adapter.RestaurantJpaAdapter;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.mapper.IDishEntityMapper;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.mapper.IRestaurantEmployeeEntityMapper;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.repository.IDishRepository;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.repository.IOrderRepository;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.repository.IRestaurantEmployeeRepository;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.repository.IRestaurantRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;
    private final EntityManager entityManager;

    private final IDishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;

    private final IRestaurantEmployeeRepository restaurantEmployeeRepository;
    private final IRestaurantEmployeeEntityMapper restaurantEmployeeEntityMapper;

    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;

    private final IUserFeignClient userFeignClient;
    private final IUserRequestMapper userRequestMapper;
    private final IUserResponseMapper userResponseMapper;

    private final IMessagingFeignClient messagingFeignClient;
    private final INotificationRequestMapper notificationRequestMapper;

    private final ITracingFeignClient tracingFeignClient;
    private final IOrderTraceRequestMapper orderTraceRequestMapper;



    @Bean
    public IRestaurantPersistencePort restaurantPersistencePort() {
        return new RestaurantJpaAdapter(
                restaurantRepository,
                restaurantEntityMapper,
                entityManager);
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
                dishEntityMapper,
                entityManager);
    }

    @Bean
    public IDishServicePort dishServicePort() {
        return new DishUseCase(
                dishPersistencePort(),
                restaurantPersistencePort(),
                securityContextPort());
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
                userClientPort(),
                securityContextPort());
    }

    @Bean
    public IOrderPersistencePort orderPersistencePort() {
        return new OrderJpaAdapter(
                orderRepository,
                orderEntityMapper,
                entityManager);
    }

    @Bean
    public IOrderServicePort orderServicePort() {
        return new OrderUseCase(
                orderPersistencePort(),
                dishPersistencePort(),
                restaurantPersistencePort(),
                restaurantEmployeePersistencePort(),
                userClientPort(),
                messagingClientPort(),
                securityContextPort(),
                tracingClientPort());
    }

    @Bean
    public IUserClientPort userClientPort() {
        return new UserClientAdapter(
                userFeignClient,
                userRequestMapper,
                userResponseMapper);
    }

    @Bean
    public IMessagingClientPort messagingClientPort() {
        return new MessagingClientAdapter(
                messagingFeignClient,
                notificationRequestMapper);
    }

    @Bean
    ISecurityContextPort securityContextPort() {
        return new SecurityContextAdapter();
    }

    @Bean
    ITracingClientPort tracingClientPort() {
        return new TracingClientAdapter(
                tracingFeignClient,
                orderTraceRequestMapper);
    }
}
