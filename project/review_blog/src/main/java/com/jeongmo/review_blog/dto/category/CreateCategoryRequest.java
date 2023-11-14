package com.jeongmo.review_blog.dto.category;

import com.jeongmo.review_blog.domain.Category;
import com.jeongmo.review_blog.util.tree.TreeUtilForCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateCategoryRequest {
    private String pathOfCategory;

    public String parent() {
        return TreeUtilForCategory.getParentOfLeaf(this.pathOfCategory);
    }

    public String name() {
        return TreeUtilForCategory.getLeafCategory(this.pathOfCategory);
    }

    public Category toEntity(Category parent) {
        return Category.builder()
                .name(name())
                .parent(parent)
                .build();
    }
}
