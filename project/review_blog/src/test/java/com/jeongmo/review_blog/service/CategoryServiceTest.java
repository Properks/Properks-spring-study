package com.jeongmo.review_blog.service;

import com.jeongmo.review_blog.domain.Category;
import com.jeongmo.review_blog.dto.category.CategoryResponse;
import com.jeongmo.review_blog.dto.category.CreateCategoryRequest;
import com.jeongmo.review_blog.dto.category.UpdateCategoryRequest;
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
        final CreateCategoryRequest request = new CreateCategoryRequest(categoryName);

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
        Category savedCategory = categoryService.createCategory(new CreateCategoryRequest(parentName + "_" + categoryName));
        Category secondCategory = categoryService.createCategory(new CreateCategoryRequest(parentName + "_" + secondName));

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
        final CreateCategoryRequest request = new CreateCategoryRequest(invalidParentName + "_" + categoryName);

        //when
        categoryService.createCategory(request);

        //then
        Category foundCategory = categoryRepository.findByName(categoryName).orElse(null);
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
        List<CategoryResponse> list = categoryService.findAllCategory().stream().map(CategoryResponse::new).toList();

        //then
        category = categoryRepository.findByName(categoryName).get();
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
        final String categoryName = "Category";
        final String parentName = "parent";
        final String siblingName = "sibling";
        final String path = "parent_Category";
        final String invalidPath = "sibling_Category";
        Category parent = categoryRepository.save(Category.builder().name(parentName).build());
        Category category = categoryRepository.save(Category.builder().name(categoryName).parent(parent).build());
        Category sibling = categoryRepository.save(Category.builder().name(siblingName).parent(parent).build());

        //when
        boolean isValid = categoryService.isValid(path);
        boolean isInvalid = categoryService.isValid(invalidPath);
        boolean noParentInPath = categoryService.isValid(siblingName);

        //then
        assertThat(isValid).isTrue();
        assertThat(isInvalid).isFalse();
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
        categoryService.deleteCategory(categoryName);

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
                () -> categoryService.deleteCategory(categoryName));

        //then
        assertThat(exception.getMessage()).isEqualTo(exceptionMessage);
    }

    @Test
    @DisplayName("updateCategoryOnlyName(): Update only name of category")
    void updateCategoryOnlyName() {
        //given
        final String categoryName = "Category";
        final String updatedNameOfCategory = "Category1";
        final String categoryPath = "Category";
        final String newCategoryPath = "Category1";

        final String childName = "Child";
        final String updatedNameOfChild= "Child1";
        final String childPath = "Category1_Child"; // After updated category
        final String newChildPath = "Category1_Child1";

        final UpdateCategoryRequest categoryRequest = new UpdateCategoryRequest(categoryPath, newCategoryPath);
        final UpdateCategoryRequest childRequest = new UpdateCategoryRequest(childPath, newChildPath);

        Category parent = categoryRepository.save(Category.builder()
                .name(categoryName)
                .build());
        Category child = categoryRepository.save(Category.builder()
                .name(childName)
                .parent(parent)
                .build());

        //when
        categoryService.updateCategory(categoryRequest);
        categoryService.updateCategory(childRequest);

        //then
        Category newCategory = categoryRepository.findByName(updatedNameOfCategory).get();
        Category newChild = categoryRepository.findByName(updatedNameOfChild).get();

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
        final String childPath = "Category_Child"; // After updated category
        final String newChildPath = "Category2_Child";

        Category parent = categoryRepository.save(Category.builder()
                .name(categoryName)
                .build());
        categoryRepository.save(Category.builder()
                .name(childName)
                .parent(parent)
                .build());
        categoryRepository.save(Category.builder()
                .name(secondCategoryName)
                .build());

        //when
        categoryService.updateCategory(new UpdateCategoryRequest(childPath, newChildPath));

        //then
        Category updatedCategory = categoryRepository.findByName(childName).get();
        Category newParentCategory = categoryRepository.findByName(secondCategoryName).get();
        Category oldParentCategory = categoryRepository.findByName(categoryName).get();

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
        final String originPath = "Category_Child";
        final String newPath = "Category2_New Child";

        Category parent = categoryRepository.save(Category.builder()
                .name(categoryName)
                .build());
        categoryRepository.save(Category.builder()
                .name(childName)
                .parent(parent)
                .build());
        categoryRepository.save(Category.builder()
                .name(secondCategoryName)
                .build());

        //when
        categoryService.updateCategory(new UpdateCategoryRequest(originPath, newPath));

        //then
        Category updatedCategory = categoryRepository.findByName(newChildName).get();
        Category newParentCategory = categoryRepository.findByName(secondCategoryName).get();
        Category oldParentCategory = categoryRepository.findByName(categoryName).get();

        assertThat(updatedCategory.getName()).isEqualTo(newChildName);
        assertThat(newParentCategory.getChildren().get(0).getName()).isEqualTo(updatedCategory.getName());

        assertThat(updatedCategory.getParent().getId()).isEqualTo(newParentCategory.getId()); // check path
        assertThat(newParentCategory.getChildren().stream().map(Category::getId)).contains(updatedCategory.getId());
        assertThat(oldParentCategory.getChildren()).isEmpty();

    }

    @Test
    @DisplayName("updateInvalidPath(): Fail to update with invalid path")
    void updateInvalidPath() {
        //given
        // Try to change child name
        final String categoryName = "Category";
        final String childName = "Child";
        final String originPath = "Category_Child";
        final String newPath = "Category_Child1";

//      originPath = "Category_Child";
        final String invalidOriginPath = "category_child";
        final String notExistName = "Category_child"; // lowercase category name
        final String invalidNewPath = "category_child";
        Category parent = categoryRepository.save(Category.builder()
                .name(categoryName)
                .build());
        categoryRepository.save(Category.builder()
                .name(childName)
                .parent(parent)
                .build());

        //when
        Category result1 = categoryService.updateCategory(new UpdateCategoryRequest(invalidOriginPath, newPath));
        Category result2 = categoryService.updateCategory(new UpdateCategoryRequest(notExistName, newPath));
        Category result3 = categoryService.updateCategory(new UpdateCategoryRequest(originPath, invalidNewPath));

        //then
        Category parentCategory = categoryRepository.findByName(categoryName).get();
        Category childCategory = categoryRepository.findByName(childName).get();

        List<Category> children = parentCategory.getChildren();

        assertNull(result1);
        assertNull(result2);
        assertNull(result3);

        assertThat(parentCategory.getName()).isEqualTo(categoryName);
        assertThat(children).hasSize(1);
        assertThat(children.stream().map(Category::getId)).contains(childCategory.getId());
        assertThat(childCategory.getName()).isEqualTo(childName);
        assertThat(childCategory.getParent().getId()).isEqualTo(parentCategory.getId());

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
        categoryRepository.save(Category.builder()
                .name(childName)
                .parent(category)
                .build());

        //when

        String resultOfParent = categoryService.getPath(parentName);
        String resultOfCategory= categoryService.getPath(categoryName);
        String resultOfChild = categoryService.getPath(childName);

        //then
        assertThat(resultOfParent).isEqualTo(parentPath);
        assertThat(resultOfCategory).isEqualTo(categoryPath);
        assertThat(resultOfChild).isEqualTo(childPath);

    }
}