package com.jeongmo.review_blog.service;

import com.jeongmo.review_blog.domain.Category;
import com.jeongmo.review_blog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    /**
     * Create Category (Add new Category into Category tree)
     *
     * @// TODO: 2023/11/02 Add parent id of category.
     * @param name The name of Category
     * @return The Created category
     */
    public Category createCategory(String name) {
        return categoryRepository.save(Category.builder()
                .name(name)
                .build());
    }

    /**
     * Find Category and return it
     *
     * @param name The name of category which you want to find
     * @return The found Category
     */
    public Category findCategory(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category"));
    }
}
