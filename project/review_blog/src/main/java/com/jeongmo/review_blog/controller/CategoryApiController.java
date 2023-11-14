package com.jeongmo.review_blog.controller;

import com.jeongmo.review_blog.domain.Category;
import com.jeongmo.review_blog.dto.category.CreateCategoryRequest;
import com.jeongmo.review_blog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CategoryApiController {

    private final CategoryService categoryService;

    @PostMapping("/api/category/{request}")
    public ResponseEntity<Category> createCategory(@PathVariable String request) {
        CreateCategoryRequest dto = new CreateCategoryRequest(request);
        Category savedCategory = categoryService.createCategory(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }
}
