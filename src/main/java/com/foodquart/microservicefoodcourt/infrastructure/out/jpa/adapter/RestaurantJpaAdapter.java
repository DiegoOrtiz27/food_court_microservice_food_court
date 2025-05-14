package com.foodquart.microservicefoodcourt.infrastructure.out.jpa.adapter;

import com.foodquart.microservicefoodcourt.domain.model.RestaurantModel;
import com.foodquart.microservicefoodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodquart.microservicefoodcourt.domain.util.Pagination;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.entity.RestaurantEntity;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.repository.IRestaurantRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RestaurantJpaAdapter implements IRestaurantPersistencePort {

    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;
    private final EntityManager entityManager;

    @Override
    public RestaurantModel saveRestaurant(RestaurantModel restaurant) {
        RestaurantEntity saved = restaurantRepository.save(restaurantEntityMapper.toEntity(restaurant));
        return restaurantEntityMapper.toRestaurantModel(saved);
    }

    @Override
    public boolean existsByNit(String nit) {
        return restaurantRepository.existsByNit(nit);
    }

    @Override
    public boolean isOwnerOfRestaurant(Long ownerId, Long restaurantId) {
        return restaurantRepository.existsByIdAndOwnerId(restaurantId, ownerId);
    }

    @Override
    public boolean existsById(Long id) {
        return restaurantRepository.existsById(id);
    }

    @Override
    public Pagination<RestaurantModel> getAllRestaurants(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("name").ascending());
        List<RestaurantModel> content = restaurantRepository.findAll(pageRequest)
                .stream()
                .map(restaurantEntityMapper::toRestaurantModel)
                .collect(Collectors.toList());

        long totalItems = getTotalRestaurantsCount();

        return new Pagination<>(
                content,
                page,
                size,
                totalItems
        );
    }

    private long getTotalRestaurantsCount() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<RestaurantEntity> root = countQuery.from(RestaurantEntity.class);
        countQuery.select(cb.count(root));
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
