package com.jeongmo.review_blog.service;

import com.jeongmo.review_blog.domain.Category;
import com.jeongmo.review_blog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category createCategory(String name) {
        return categoryRepository.save(Category.builder()
                .name(name)
                .build());
    }

    public Category findCategory(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category"));
    }
}
