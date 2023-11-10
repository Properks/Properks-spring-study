package com.jeongmo.review_blog.service;

import com.jeongmo.review_blog.domain.Category;
import com.jeongmo.review_blog.dto.category.CreateCategoryRequest;
import com.jeongmo.review_blog.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryServiceTest {

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    void initRepository() {
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("createCategoryWithoutParent(): Success to create category without parent category")
    void createCategoryWithoutParent() {
        //given
        final String categoryName = "CategoryName";
        final String parentName = null;
        final CreateCategoryRequest request = new CreateCategoryRequest(categoryName, parentName);

        //when
        Category savedCategory = categoryService.createCategory(request);

        //then
        assertThat(savedCategory.getName()).isEqualTo(categoryName);
        assertThat(savedCategory.getParent()).isEqualTo(null);

    }

    @Test
    @DisplayName("createCategoryWithParent(): Success to create category with parent category")
    void createCategoryWithParent() {
        //given
        final String categoryName = "CategoryName";
        final String parentName = "ParentCategory";
        final String secondName = "A SecondCategory";
        categoryRepository.save(Category.builder()
                .name(parentName)
                .parent(null)
                .build());

        //when
        Category savedCategory = categoryService.createCategory(new CreateCategoryRequest(categoryName, parentName));
        Category secondCategory = categoryService.createCategory(new CreateCategoryRequest(secondName, parentName));

        //then
        Category parentCategory = categoryRepository.findByName(parentName).get();

        assertThat(savedCategory.getName()).isEqualTo(categoryName);
        assertThat(secondCategory.getName()).isEqualTo(secondName);

        assertThat(savedCategory.getParent().getId()).isEqualTo(parentCategory.getId());
        assertThat(secondCategory.getParent().getId()).isEqualTo(parentCategory.getId());

        List<Category> children = parentCategory.getChildren();
        assertThat(children).hasSize(2);
        assertThat(children.get(0).getId()).isEqualTo(secondCategory.getId());
        assertThat(children.get(1).getId()).isEqualTo(savedCategory.getId());
        // The order was changed in the getChildren().
    }

    @Test
    @DisplayName("createCategoryInvalidParent(): success to create category with invalid parent name")
    void createCategoryInvalidParent() {
        //given
        final String categoryName = "category";
        final String invalidParentName = "invalid";
        final String exceptionMessage = "Invalid parent category in request";
        final CreateCategoryRequest request = new CreateCategoryRequest(categoryName, invalidParentName);

        //when
        Exception exception
        = assertThrows(IllegalArgumentException.class, () -> categoryService.createCategory(request));

        //then
        Category foundCategory = categoryRepository.findByName(categoryName).orElse(null);
        assertThat(exception.getMessage()).isEqualTo(exceptionMessage);
        assertThat(foundCategory).isNull();

    }
}