package com.example.restaurant.repository;

import com.example.restaurant.model.entity.MenuItem;
import com.example.restaurant.model.enums.Category;
import com.example.restaurant.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id = :restaurantId AND m.deleted = false")
    Page<MenuItem> findByRestaurantId(@Param("restaurantId") Long restaurantId, Pageable pageable);

    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id = :restaurantId AND m.category = :category AND m.deleted = false")
    Page<MenuItem> findByRestaurantIdAndCategory(
        @Param("restaurantId") Long restaurantId,
        @Param("category") Category category,
        Pageable pageable
    );

    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id = :restaurantId AND m.status = :status AND m.deleted = false")
    Page<MenuItem> findByRestaurantIdAndStatus(
        @Param("restaurantId") Long restaurantId,
        @Param("status") Status status,
        Pageable pageable
    );

    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id = :restaurantId AND m.category = :category AND m.status = :status AND m.deleted = false")
    Page<MenuItem> findByRestaurantIdAndCategoryAndStatus(
        @Param("restaurantId") Long restaurantId,
        @Param("category") Category category,
        @Param("status") Status status,
        Pageable pageable
    );

    @Query("SELECT m FROM MenuItem m WHERE m.id = :id AND m.deleted = false")
    Optional<MenuItem> findByIdAndNotDeleted(@Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM MenuItem m WHERE m.name = :name AND m.restaurant.id = :restaurantId AND m.deleted = false")
    boolean existsByNameAndRestaurantId(@Param("name") String name, @Param("restaurantId") Long restaurantId);
}
