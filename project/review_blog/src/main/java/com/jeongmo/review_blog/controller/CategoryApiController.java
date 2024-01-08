package com.jeongmo.review_blog.controller;

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
        try {
            CategoryResponse savedCategory = new CategoryResponse(categoryService.createCategory(request));
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
        }
        catch(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
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
        try {
            CategoryResponse foundCategory = new CategoryResponse(categoryService.findCategory(name));
            return ResponseEntity.ok().body(foundCategory);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/api/category/{name}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String name) {
        try {
            categoryService.deleteCategory(name);
            return ResponseEntity.ok().build();
        }
        catch(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/api/category")
    public ResponseEntity<CategoryResponse> updateCategory(@RequestBody UpdateCategoryRequest request) {
        try {
            CategoryResponse updatedCategory = new CategoryResponse(categoryService.updateCategory(request));
            return ResponseEntity.ok().body(updatedCategory);
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
