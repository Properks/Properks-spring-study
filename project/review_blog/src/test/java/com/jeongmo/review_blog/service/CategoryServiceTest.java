package com.jeongmo.review_blog.service;

import com.jeongmo.review_blog.domain.Category;
import com.jeongmo.review_blog.dto.article_view.CategoryResponse;
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
    @DisplayName("createCategoryInvalidParent(): Fail to create category with invalid parent name")
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

    @Test
    @DisplayName("findOneValidCategory(): Success to find category")
    void findOneValidCategory() {
        //given
        final String categoryName = "Category";
        Category savedCategory = categoryRepository.save(Category.builder()
                        .name(categoryName)
                .build());

        //when
        Category foundCategory = categoryService.findCategory(categoryName);

        //then
        assertThat(foundCategory.getId()).isEqualTo(savedCategory.getId());
        assertThat(foundCategory.getName()).isEqualTo(savedCategory.getName());
        assertThat(foundCategory.getParent()).isEqualTo(savedCategory.getParent());
        assertThat(foundCategory.getChildren()).isEqualTo(savedCategory.getChildren());
    }

    @Test
    @DisplayName("findOneInvalidCategory(): Fail to find category")
    void findOneInvalidCategory() {
        //given
        final String categoryName = "categoryName";
        final String nameParameter = "Category";
        final String exceptionMessage = "Invalid category";
        categoryRepository.save(Category.builder()
                .name(categoryName)
                .build());

        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> categoryService.findCategory(nameParameter));

        //then
        assertThat(exception.getMessage()).isEqualTo(exceptionMessage);

    }

    @Test
    @DisplayName("findAllCategory(): Success to find all category")
    void findAllCategory() {
        //given
        final String categoryName = "Category";
        final String siblingCategory = "siblingCategory";
        final String childCategory = "childCategory";
        Category category = categoryRepository.save(Category.builder()
                .name(categoryName)
                .build());
        Category sibling= categoryRepository.save(Category.builder()
                .name(siblingCategory)
                .build());
        Category child = categoryRepository.save(Category.builder()
                .name(childCategory)
                .parent(category)
                .build());

        //when
        List<CategoryResponse> list = categoryService.findAllCategory();

        //then
        category = categoryRepository.findByName(categoryName).get();
        assertThat(list).hasSize(3);
        assertThat(list.get(0).getId()).isEqualTo(category.getId());
        assertThat(list.get(0).getName()).isEqualTo(category.getName());
        assertThat(list.get(0).getHeight()).isZero();

        assertThat(list.get(1).getId()).isEqualTo(sibling.getId());
        assertThat(list.get(1).getName()).isEqualTo(sibling.getName());
        assertThat(list.get(1).getHeight()).isZero();

        assertThat(list.get(2).getId()).isEqualTo(child.getId());
        assertThat(list.get(2).getName()).isEqualTo(child.getName());
        assertThat(list.get(2).getHeight()).isEqualTo(1);

    }
}