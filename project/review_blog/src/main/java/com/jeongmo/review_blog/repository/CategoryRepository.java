package com.jeongmo.review_blog.repository;

import com.jeongmo.review_blog.domain.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    /**
     * Find Category with name
     */
    @EntityGraph(attributePaths = { "children" })
    Optional<Category> findByName(String name);

    /**
     * Find All root categories.
     */
    @Query("SELECT category FROM Category category WHERE category.parent IS NULL")
    List<Category> findRootCategories();

    boolean existsByName(String name);
}
