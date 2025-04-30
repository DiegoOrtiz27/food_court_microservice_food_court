package com.foodquart.microservicefoodcourt.infrastructure.configuration;

import com.foodquart.microservicefoodcourt.domain.api.ICreateDishServicePort;
import com.foodquart.microservicefoodcourt.domain.api.IRestaurantServicePort;
import com.foodquart.microservicefoodcourt.domain.api.IUpdateDishServicePort;
import com.foodquart.microservicefoodcourt.domain.spi.IDishPersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodquart.microservicefoodcourt.domain.spi.IUserClientPort;
import com.foodquart.microservicefoodcourt.domain.usecase.CreateCreateDishUseCase;
import com.foodquart.microservicefoodcourt.domain.usecase.CreateRestaurantUseCase;
import com.foodquart.microservicefoodcourt.domain.usecase.UpdateDishUseCase;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.IUserFeignClient;
import com.foodquart.microservicefoodcourt.infrastructure.out.client.adapter.UserClientAdapter;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.adapter.DishJpaAdapter;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.adapter.RestaurantJpaAdapter;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.mapper.IDishEntityMapper;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.repository.IDishRepository;
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

    private final IUserFeignClient userFeignClient;

    @Bean
    public IRestaurantPersistencePort restaurantPersistencePort() {
        return new RestaurantJpaAdapter(
                restaurantRepository,
                restaurantEntityMapper);
    }

    @Bean
    public IRestaurantServicePort restaurantServicePort() {
        return new CreateRestaurantUseCase(
                restaurantPersistencePort(),
                userClientPort());
    }

    @Bean
    public IDishPersistencePort dishPersistencePort() {
        return new DishJpaAdapter(dishRepository, dishEntityMapper);
    }

    @Bean
    public ICreateDishServicePort createDishServicePort() {
        return new CreateCreateDishUseCase(
                dishPersistencePort(),
                restaurantPersistencePort()
        );
    }

    @Bean
    public IUpdateDishServicePort updateDishServicePort() {
        return new UpdateDishUseCase(
                dishPersistencePort(),
                restaurantPersistencePort()
        );
    }

    @Bean
    public IUserClientPort userClientPort() {
        return new UserClientAdapter(userFeignClient);
    }
}
