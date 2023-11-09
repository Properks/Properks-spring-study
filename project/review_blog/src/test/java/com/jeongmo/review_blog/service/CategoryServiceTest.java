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
        categoryRepository.save(Category.builder()
                .name(parentName)
                .parent(null)
                .build());

        //when
        Category savedCategory = categoryService.createCategory(new CreateCategoryRequest(categoryName, parentName));

        //then
        Category parentCategory = categoryRepository.findByName(parentName).get();

        assertThat(savedCategory.getName()).isEqualTo(categoryName);
        assertThat(savedCategory.getParent().getId()).isEqualTo(parentCategory.getId());
        assertThat(parentCategory.getChildren()).hasSize(1);
        assertThat(parentCategory.getChildren().get(0).getId()).isEqualTo(savedCategory.getId());

    }
}