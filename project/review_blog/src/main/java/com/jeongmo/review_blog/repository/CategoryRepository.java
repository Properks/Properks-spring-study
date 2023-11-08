package com.jeongmo.review_blog.repository;

import com.jeongmo.review_blog.domain.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    /**
     * Find Category with name
     */
    @EntityGraph(attributePaths = { "children" })
    Optional<Category> findByName(String name);
    boolean existsByName(String name);
}
