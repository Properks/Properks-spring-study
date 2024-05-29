package com.jeongmo.review_blog.repository;

import com.jeongmo.review_blog.domain.Category;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    /**
     * Find Category with id and EntityGraph to read children
     */
    @EntityGraph(attributePaths = { "children" })
    @Override
    @NotNull
    Optional<Category> findById(@NotNull Long id);

    /**
     * Find All root categories.
     */
    @Query("SELECT category FROM Category category WHERE category.parent IS NULL")
    List<Category> findRootCategories();

    boolean existsByName(String name);
}
