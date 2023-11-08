package com.jeongmo.review_blog.controller;

import com.jeongmo.review_blog.domain.Category;
import com.jeongmo.review_blog.dto.category.CreateCategoryRequest;
import com.jeongmo.review_blog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class CategoryApiController {

    private final CategoryService categoryService;

    @PostMapping("/api/category")
    public ResponseEntity<Category> createCategory(CreateCategoryRequest request) {
        Category savedCategory = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }
}
