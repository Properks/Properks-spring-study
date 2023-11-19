package com.jeongmo.review_blog.service;

import com.jeongmo.review_blog.domain.Category;
import com.jeongmo.review_blog.dto.category.CreateCategoryRequest;
import com.jeongmo.review_blog.dto.category.UpdateCategoryRequest;
import com.jeongmo.review_blog.repository.CategoryRepository;
import com.jeongmo.review_blog.util.tree.TreeUtilForCategory;
import jakarta.transaction.Transactional;
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
        if (!isValid(TreeUtilForCategory.getPathWithoutLeaf(request.getPathOfCategory()))) {return null;}

        Category parent = (request.parent() != null) ? categoryRepository.findByName(request.parent()).
                orElseThrow(() -> new IllegalArgumentException("Invalid parent category in request")) : null;
        // When Cannot find, Throw Exception except that parent is null.
        return categoryRepository.save(request.toEntity(parent));
//        if (parent != null) { // Doesn't need it because of CascadeType.ALL
//            parent.addChild(child);
//        }
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

    public List<Category> findAllCategory() {
        List<Category> roots = categoryRepository.findRootCategories();
        roots.sort(Category::compareTo);
        List<Category> sortedCategories = new ArrayList<>();
        return addChildrenRecursive(sortedCategories, roots);
    }

    /**
     * Delete category by name. If the category value is null or does not exist or has children, it cannot be deleted.
     *
     * @param  name The name of category which will be deleted.
     */
    public void deleteCategory(String name) {
        if (name == null || !isExist(name)) {throw new IllegalArgumentException("Invalid category");}
        // when name is invalid

        Category foundCategory = findCategory(name);
        if (!foundCategory.getChildren().isEmpty()) { // When Category has one or more child category
            throw new IllegalArgumentException("Fail to delete because " + name +" has children");
        }
        categoryRepository.deleteById(foundCategory.getId());

        // When i didn't use CascadeType
//        if (foundCategory.getParent() == null) {
//            categoryRepository.deleteById(foundCategory.getId());
//        }
//        else { // Category isn't root node
//            foundCategory.getParent().getChildren().remove(foundCategory);
//            categoryRepository.deleteById(foundCategory.getId());
//        }
    }

    /**
     * Update category. The path must be valid for update and the category of originPath must exist. You should also not have children if you are rerouting.
     *
     * @param request The request which has originPath and newPath to update
     * @return The updated category
     */
    @Transactional
    public Category updateCategory(UpdateCategoryRequest request) {
        String originCategoryName = TreeUtilForCategory.getLeafCategory(request.getOriginPath());
        String newCategoryName = TreeUtilForCategory.getLeafCategory(request.getNewPath());
        if (!(isValid(request.getOriginPath())
                && isValid(TreeUtilForCategory.getPathWithoutLeaf(request.getNewPath()))
                && isExist(originCategoryName))) { // When Category path is invalid or Category doesn't exist
            return null;
        }

        Category foundCategory = findCategory(originCategoryName);
        if (request.getOriginPath().equals(request.getNewPath())) {return foundCategory;} // Doesn't need to update

        // Do update
        if (((TreeUtilForCategory.getPathWithoutLeaf(request.getOriginPath()) == null) && (TreeUtilForCategory.getPathWithoutLeaf(request.getNewPath()) == null))
                || TreeUtilForCategory.getPathWithoutLeaf(request.getOriginPath()).equals(TreeUtilForCategory.getPathWithoutLeaf(request.getNewPath()))) { // update only name
            foundCategory.update(newCategoryName);
            return foundCategory;
        }

        if (!foundCategory.getChildren().isEmpty()) { // When Category has one or more child category for updating path
            return null;
        }
        else if (originCategoryName.equals(newCategoryName)) { // update only path
            foundCategory.setParent(findCategory(TreeUtilForCategory.getParentOfLeaf(request.getNewPath())));
        }
        else { // update all of them
            foundCategory.setParent(findCategory(TreeUtilForCategory.getParentOfLeaf(request.getNewPath())));
            foundCategory.update(newCategoryName);
        }
        return foundCategory;
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
     * Check whether path is valid or not except child. Split path to category names and check ith category has (i +
     * 1)th category
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
                    orElse(null);
            return category != null && category.getParent() == null;
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

    private List<Category> addChildrenRecursive(List<Category> list, List<Category> children) {
        // Recursively sort the children's children
        for (Category child : children) {
            list.add(child);
            addChildrenRecursive(list, child.getChildren());
        }

        return list; // Return the sorted list
    }
}
