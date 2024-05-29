package com.jeongmo.review_blog.service;

import com.jeongmo.review_blog.domain.Category;
import com.jeongmo.review_blog.dto.category.CategoryResponse;
import com.jeongmo.review_blog.dto.category.CreateCategoryRequest;
import com.jeongmo.review_blog.dto.category.UpdateCategoryRequest;
import com.jeongmo.review_blog.repository.ArticleRepository;
import com.jeongmo.review_blog.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    ArticleRepository articleRepository;

    @BeforeEach
    void initRepository() {
        articleRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("createCategoryWithoutParent(): Success to create category without parent category")
    void createCategoryWithoutParent() {
        //given
        final String categoryName = "CategoryName";
        final CreateCategoryRequest request = new CreateCategoryRequest(null, categoryName);

        //when
        Category savedCategory = categoryService.createCategory(request);

        //then
        assertThat(savedCategory.getName()).isEqualTo(categoryName);
        assertThat(savedCategory.getParent()).isNull();

    }

    @Test
    @DisplayName("createCategoryWithParent(): Success to create category with parent category")
    void createCategoryWithParent() {
        //given
        final String categoryName = "CategoryName";
        final String parentName = "ParentCategory";
        final String secondName = "A SecondCategory";
        Category parent = categoryRepository.save(Category.builder()
                .name(parentName)
                .parent(null)
                .build());

        //when
        Category savedCategory = categoryService.createCategory(new CreateCategoryRequest(parent.getId(), categoryName));
        Category secondCategory = categoryService.createCategory(new CreateCategoryRequest(parent.getId(), secondName));

        //then
        Category parentCategory = categoryRepository.findById(parent.getId()).get();

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
        final CreateCategoryRequest request = new CreateCategoryRequest((long)-1, categoryName);

        //when
        assertThrows(IllegalArgumentException.class,() -> categoryService.createCategory(request));

        //then
        List<Category> foundCategory = categoryRepository.findAll();
        assertThat(foundCategory).isEmpty();

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
        Category foundCategory = categoryService.findCategory(savedCategory.getId());

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
        final String exceptionMessage = "Not found category has -1";
        categoryRepository.save(Category.builder()
                .name(categoryName)
                .build());

        //when
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> categoryService.findCategory((long)-1));

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
        List<CategoryResponse> list = categoryService.findAllCategory().stream().map(CategoryResponse::new).toList();

        //then
        category = categoryRepository.findById(category.getId()).get();
        assertThat(list).hasSize(3);
        assertThat(list.get(0).getId()).isEqualTo(category.getId());
        assertThat(list.get(0).getName()).isEqualTo(category.getName());
        assertThat(list.get(0).getHeight()).isZero();

        assertThat(list.get(1).getId()).isEqualTo(child.getId());
        assertThat(list.get(1).getName()).isEqualTo(child.getName());
        assertThat(list.get(1).getHeight()).isEqualTo(1);

        assertThat(list.get(2).getId()).isEqualTo(sibling.getId());
        assertThat(list.get(2).getName()).isEqualTo(sibling.getName());
        assertThat(list.get(2).getHeight()).isZero();

    }

    @Test
    @DisplayName("isExist(): Check whether name of category is valid or not")
    void isExist() {
        //given
        final String categoryName = "Category";
        Category category = Category.builder()
                .name(categoryName)
                .build();
        categoryRepository.save(category);

        //when
        boolean isValid = categoryService.isExistedName(categoryName);

        //then
        assertThat(isValid).isTrue();

        //given
        categoryRepository.deleteById(category.getId());
        categoryRepository.flush();

        //when
        boolean isInvalid = categoryService.isExistedName(categoryName);

        //then
        assertThat(isInvalid).isFalse();
    }

    @Test
    @DisplayName("isValid(): Check Whether path is valid or not")
    void isValid() {
        //given
        /* structure
        *  parent - Category
        *         - sibling
        */
        final String parentName = "parent";
        Category parent = categoryRepository.save(Category.builder().name(parentName).build());

        //when
        boolean isValid = categoryService.isValid(parent.getId());
        boolean noParentInPath = categoryService.isValid((long)-1);

        //then
        assertThat(isValid).isTrue();
        assertThat(noParentInPath).isFalse();
    }

    @Test
    @DisplayName("deleteCategory(): Success to delete category")
    void deleteCategory() {
        //given
        final String categoryName = "Category";
        Category savedCategory = categoryRepository.save(Category.builder()
                .name(categoryName)
                .build());

        //when
        categoryService.deleteCategory(savedCategory.getId());

        //then
        assertThat(categoryRepository.findAll()).isEmpty();

    }

    @Test
    @DisplayName("deleteCategoryWithChildren(): Fail to delete Category because of children")
    void deleteCategoryWithChildren() {
        //given
        final String categoryName = "Category";
        final String childName = "Child Category";
        final String exceptionMessage = "Fail to delete because " + categoryName +" has children";
        Category category = categoryRepository.save(Category.builder()
                .name(categoryName)
                .build());
        Category child = categoryRepository.save(Category.builder()
                .name(childName)
                .parent(category)
                .build());

        //when
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> categoryService.deleteCategory(category.getId()));

        //then
        assertThat(exception.getMessage()).isEqualTo(exceptionMessage);
    }

    @Test
    @DisplayName("updateCategoryOnlyName(): Update only name of category")
    void updateCategoryOnlyName() {
        //given
        final String categoryName = "Category";
        final String updatedNameOfCategory = "Category1";

        final String childName = "Child";
        final String updatedNameOfChild= "Child1";

        Category parent = categoryRepository.save(Category.builder()
                .name(categoryName)
                .build());
        Category child = categoryRepository.save(Category.builder()
                .name(childName)
                .parent(parent)
                .build());

        final UpdateCategoryRequest categoryRequest = new UpdateCategoryRequest(parent.getId(), null, updatedNameOfCategory);
        final UpdateCategoryRequest childRequest = new UpdateCategoryRequest(child.getId(), parent.getId(), updatedNameOfChild);

        //when
        categoryService.updateCategory(categoryRequest);
        categoryService.updateCategory(childRequest);

        //then
        Category newCategory = categoryRepository.findById(parent.getId()).get();
        Category newChild = categoryRepository.findById(child.getId()).get();

        assertThat(newCategory.getId()).isEqualTo(parent.getId());
        assertThat(newChild.getId()).isEqualTo(child.getId());

        assertThat(newCategory.getChildren().get(0).getId()).isEqualTo(newChild.getId());
        assertThat(newCategory.getChildren().get(0).getName()).isEqualTo(newChild.getName());

    }

    @Test
    @DisplayName("updateCategoryOnlyPath(): update only path")
    void updateCategoryOnlyPath() {
        //given
        final String categoryName = "Category";

        final String secondCategoryName = "Category2";

        final String childName = "Child";

        Category parent = categoryRepository.save(Category.builder()
                .name(categoryName)
                .build());
        Category child = categoryRepository.save(Category.builder()
                .name(childName)
                .parent(parent)
                .build());
        Category parent2 = categoryRepository.save(Category.builder()
                .name(secondCategoryName)
                .build());

        //when
        categoryService.updateCategory(new UpdateCategoryRequest(child.getId(), parent2.getId(), childName));

        //then
        Category updatedCategory = categoryRepository.findById(child.getId()).get();
        Category newParentCategory = categoryRepository.findById(parent2.getId()).get();
        Category oldParentCategory = categoryRepository.findById(parent.getId()).get();

        assertThat(updatedCategory.getParent().getId()).isEqualTo(newParentCategory.getId());
        assertThat(newParentCategory.getChildren().stream().map(Category::getId)).contains(updatedCategory.getId());
        assertThat(oldParentCategory.getChildren()).isEmpty();

    }

    @Test
    @DisplayName("updateCategoryBoth(): update name and path")
    void updateCategoryBoth() {
        //given
        final String categoryName = "Category";

        final String secondCategoryName = "Category2";

        final String childName = "Child";
        final String newChildName = "New Child";

        Category parent = categoryRepository.save(Category.builder()
                .name(categoryName)
                .build());
        Category child = categoryRepository.save(Category.builder()
                .name(childName)
                .parent(parent)
                .build());
        Category parent2 = categoryRepository.save(Category.builder()
                .name(secondCategoryName)
                .build());

        //when
        categoryService.updateCategory(new UpdateCategoryRequest(child.getId(), parent2.getId(), newChildName));

        //then
        Category updatedCategory = categoryRepository.findById(child.getId()).get();
        Category newParentCategory = categoryRepository.findById(parent2.getId()).get();
        Category oldParentCategory = categoryRepository.findById(parent.getId()).get();

        assertThat(updatedCategory.getName()).isEqualTo(newChildName);
        assertThat(newParentCategory.getChildren().get(0).getName()).isEqualTo(updatedCategory.getName());

        assertThat(updatedCategory.getParent().getId()).isEqualTo(newParentCategory.getId()); // check path
        assertThat(newParentCategory.getChildren().stream().map(Category::getId)).contains(updatedCategory.getId());
        assertThat(oldParentCategory.getChildren()).isEmpty();

    }

    @Test
    @DisplayName("getPath(): Success to get path")
    void getPath() {
        //given
        final String parentName= "parent";
        final String categoryName = "category";
        final String childName= "child";

        final String parentPath = "parent";
        final String categoryPath = "parent_category";
        final String childPath = "parent_category_child";

        Category parent = categoryRepository.save(Category.builder().name(parentName).build());
        Category category = categoryRepository.save(Category.builder()
                .name(categoryName)
                .parent(parent)
                .build());
        Category child = categoryRepository.save(Category.builder()
                .name(childName)
                .parent(category)
                .build());

        //when

        String resultOfParent = categoryService.getPath(parent.getId());
        String resultOfCategory= categoryService.getPath(category.getId());
        String resultOfChild = categoryService.getPath(child.getId());

        //then
        assertThat(resultOfParent).isEqualTo(parentPath);
        assertThat(resultOfCategory).isEqualTo(categoryPath);
        assertThat(resultOfChild).isEqualTo(childPath);

    }
}