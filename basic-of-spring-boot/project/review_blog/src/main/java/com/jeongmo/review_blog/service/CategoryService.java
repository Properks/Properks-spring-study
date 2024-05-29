package com.jeongmo.review_blog.service;

import com.jeongmo.review_blog.domain.Category;
import com.jeongmo.review_blog.dto.category.CreateCategoryRequest;
import com.jeongmo.review_blog.dto.category.UpdateCategoryRequest;
import com.jeongmo.review_blog.repository.CategoryRepository;
import com.jeongmo.review_blog.util.tree.TreeUtilForCategory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    /**
     * Create Category (Add new Category into Category tree)
     *
     * @param request The name of Category.
     * @return The Created category.
     */
    public Category createCategory(@NotNull CreateCategoryRequest request) {
        if (request.getParentId() != null && !isValid(request.getParentId())) {
            throw new IllegalArgumentException("Invalid parent category in request");
        }

        Category parent = (request.getParentId() != null) ? categoryRepository.findById(request.getParentId()).
                orElseThrow(() -> new IllegalArgumentException("Invalid parent category in request")) : null;
        // When Cannot find, Throw Exception except that parent is null.
        return categoryRepository.save(Category.builder()
                .name(request.getName())
                .parent(parent)
                .build());
    }


    /**
     * Find category with id
     *
     *
     */
    public Category findCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Not found category has " + id));
    }

    /**
     * Find All categories with recursive order
     *
     * @return
     * if there are two parent nodes (parent1, parent2). parent1 has two child node (child1, child2) and parent2 has
     * a child node (child3), Return is
     * list = {parent1, child1, child2, parent2, child3}
     *
     */
    @Transactional(readOnly = true)
    // when I use readOnly = true, JPA recognizes that the entity is for inquiry purposes
    public List<Category> findAllCategory() {
        List<Category> roots = categoryRepository.findRootCategories();
        roots.sort(Category::compareTo);
        List<Category> sortedCategories = new ArrayList<>();
        return addChildrenRecursive(sortedCategories, roots);
    }

    /**
     * Delete category by name. If the category value is null or does not exist or has children, it cannot be deleted.
     *
     * @param id The id of category which will be deleted.
     */
    public void deleteCategory(Long id) {
        if (id == null || !isValid(id)) {throw new IllegalArgumentException("Invalid category");}
        // when name is invalid

        Category foundCategory = categoryRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Invalid category"));
        if (!foundCategory.getChildren().isEmpty()) { // When Category has one or more child category
            throw new IllegalArgumentException("Fail to delete because " + foundCategory.getName() +" has children");
        }
        categoryRepository.deleteById(id);
    }

    /**
     * Update category. The path must be valid for update and the category of originPath must exist. You should also not have children if you are rerouting.
     *
     * @param request The request which has originPath and newPath to update
     * @return The updated category
     */
    @Transactional
    public Category updateCategory(UpdateCategoryRequest request) {
        Category updatedCategory = categoryRepository.findById(request.getCategoryId()).orElseThrow( // find category
                () -> new IllegalArgumentException("Category isn't existed")
        );
        if (request.getNewParent() != null) {
            if (updatedCategory.getId().equals(request.getNewParent()) || !isValid(request.getNewParent())) { // Parent
                // error
                throw new IllegalArgumentException("Invalid parent id");
            }
            if (!request.getNewParent().equals(updatedCategory.getParent().getId())) { // When parent is changed
                Category parent = findCategory(request.getNewParent());
                updatedCategory.setParent(parent);
            }
        }
        return updatedCategory.update(request.getNewName());
    }

    /**
     * Check whether category exists or not
     *
     * @param name The name of category
     * @return The boolean, If category exists, true
     */
    public boolean isExistedName(String name) {
        return categoryRepository.existsByName(name);
    }

    public boolean isEmpty() {
        return categoryRepository.count() == 0;
    }

    /**
     * Check whether path is valid or not except child. Split path to category names and check ith category has (i +
     * 1)th category
     * as child (for i = 0, 1, ....)
     *
     * @param id The id of category which you want to check
     * @return The boolean, true: valid, false: category doesn't exist
     */
    public boolean isValid(Long id) {
        return id == null || categoryRepository.existsById(id);
    }

    /**
     * Get the path of category.
     *
     * @param id The id of Category.
     * @return The path of category
     */
    @Transactional(readOnly = true)
    public String getPath(Long id) {
        return findCategory(id).getPath();
    }

    /**
     * Make a list which is sorted with parent, child order
     *
     * @param list The empty list which you want to save
     * @param children The list which contains all root node
     * @return The sorted list
     */
    private List<Category> addChildrenRecursive(List<Category> list, List<Category> children) {
        // Recursively sort the children's children
        for (Category child : children) {
            list.add(child);
            addChildrenRecursive(list, child.getChildren());
        }

        return list; // Return the sorted list
    }
}
