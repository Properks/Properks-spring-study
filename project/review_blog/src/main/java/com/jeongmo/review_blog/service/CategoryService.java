package com.jeongmo.review_blog.service;

import com.jeongmo.review_blog.domain.Category;
import com.jeongmo.review_blog.dto.article_view.CategoryResponse;
import com.jeongmo.review_blog.dto.category.CreateCategoryRequest;
import com.jeongmo.review_blog.repository.CategoryRepository;
import com.jeongmo.review_blog.util.tree.TreeUtilForCategory;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

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
        if (!isValid(TreeUtilForCategory.getPathWithoutLeaf(request.getPathOfCategory()))) {return null;}

        Category parent = (request.parent() != null) ? categoryRepository.findByName(request.parent()).
                orElseThrow(() -> new IllegalArgumentException("Invalid parent category in request")) : null;
        // When Cannot find, Throw Exception except that parent is null.
        Category child = categoryRepository.save(request.toEntity(parent));
        if (parent != null) {
            parent.addChild(child);
        }
        return child;
    }

    /**
     * Find Category and return it
     *
     * @param name The name of category which you want to find
     * @return The found Category
     */
    public Category findCategory(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category"));
    }

    public List<CategoryResponse> findAllCategory() {
        return categoryRepository.findAll().
                stream().map(CategoryResponse::new).toList();
    }

    /**
     * Check whether category exists or not
     *
     * @param name The name of category
     * @return The boolean, If category exists, true
     */
    public boolean isExist(String name) {
        return categoryRepository.existsByName(name);
    }

    /**
     * Check whether path is valid or not. Split path to category names and check ith category has (i + 1)th category
     * as child (for i = 0, 1, ....)
     *
     * @param path The path of category which you want to check
     * @return The boolean, true: valid, false: category doesn't exist or have next category as child.
     */
    public boolean isValid(String path) {
        if (path == null) {return true;}

        String[] paths = path.split("_");

        if (paths.length == 1) { // when root node
            Category category = categoryRepository.findByName(path).
                    orElseThrow(() -> new IllegalArgumentException("Invalid parent category in request"));
            return category.getParent() == null;
        }

        for (int i = 0; i < paths.length - 1; i++) { // when child node
            if (isExist(paths[i]) && isExist(paths[i + 1])) {
                Category baseCategory = findCategory(paths[i]);
                Category childCategory = findCategory(paths[i + 1]);
                if (!(baseCategory.getChildren().stream().map(Category::getId).toList().contains(childCategory.getId())
                        && childCategory.getParent().getId().equals(baseCategory.getId()))) {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        return true;
    }
}