package com.foodquart.microservicefoodcourt.infrastructure.out.jpa.adapter;

import com.foodquart.microservicefoodcourt.domain.model.DishModel;
import com.foodquart.microservicefoodcourt.domain.spi.IDishPersistencePort;
import com.foodquart.microservicefoodcourt.domain.util.Pagination;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.entity.DishEntity;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.mapper.IDishEntityMapper;
import com.foodquart.microservicefoodcourt.infrastructure.out.jpa.repository.IDishRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DishJpaAdapter implements IDishPersistencePort {

    private final IDishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;
    private final EntityManager entityManager;

    @Override
    public DishModel saveDish(DishModel dish) {
        DishEntity dishEntity = dishEntityMapper.toEntity(dish);
        DishEntity savedEntity = dishRepository.save(dishEntity);
        return dishEntityMapper.toDishModel(savedEntity);
    }

    @Override
    public Optional<DishModel> findById(Long id) {
        return dishRepository.findById(id)
                .map(dishEntityMapper::toDishModel);
    }

    @Override
    public Pagination<DishModel> findByRestaurantIdAndCategory(Long restaurantId, String category, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<DishEntity> resultPage = (category == null || category.isEmpty())
                ? dishRepository.findByRestaurantId(restaurantId, pageRequest)
                : dishRepository.findByRestaurantIdAndCategory(restaurantId, category, pageRequest);

        List<DishModel> content = resultPage.getContent()
                .stream()
                .map(dishEntityMapper::toDishModel)
                .collect(Collectors.toList());

        return new Pagination<>(
                content,
                page,
                size,
                getTotalDishesCount(restaurantId, category)
        );
    }

    private long getTotalDishesCount(Long restaurantId, String category) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<DishEntity> root = countQuery.from(DishEntity.class);

        Predicate restaurantPredicate = cb.equal(root.get("restaurant").get("id"), restaurantId);

        if (category != null && !category.isEmpty()) {
            Predicate categoryPredicate = cb.equal(root.get("category"), category);
            countQuery.where(cb.and(restaurantPredicate, categoryPredicate));
        } else {
            countQuery.where(restaurantPredicate);
        }

        countQuery.select(cb.count(root));
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
