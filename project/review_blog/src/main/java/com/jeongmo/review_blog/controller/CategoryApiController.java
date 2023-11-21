package com.jeongmo.review_blog.controller;

import com.jeongmo.review_blog.domain.Category;
import com.jeongmo.review_blog.dto.category.CategoryResponse;
import com.jeongmo.review_blog.dto.category.CreateCategoryRequest;
import com.jeongmo.review_blog.dto.category.UpdateCategoryRequest;
import com.jeongmo.review_blog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CategoryApiController {

    private final CategoryService categoryService;

    @PostMapping("/api/category")
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CreateCategoryRequest request) {
        CategoryResponse savedCategory = new CategoryResponse(categoryService.createCategory(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    @GetMapping("/api/category")
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        List<CategoryResponse> list = categoryService.findAllCategory()
                .stream()
                .map(CategoryResponse::new)
                .toList();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/api/category/{name}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable String name) {
        CategoryResponse foundCategory = new CategoryResponse(categoryService.findCategory(name));
        return ResponseEntity.ok().body(foundCategory);
    }

    @DeleteMapping("/api/category/{name}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String name) {
        categoryService.deleteCategory(name);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/category")
    public ResponseEntity<CategoryResponse> updateCategory(@RequestBody UpdateCategoryRequest request) {
        CategoryResponse updatedCategory = new CategoryResponse(categoryService.updateCategory(request));
        return ResponseEntity.ok().body(updatedCategory);
    }

}
