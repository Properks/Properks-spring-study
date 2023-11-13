package com.jeongmo.review_blog.dto.category;

import com.jeongmo.review_blog.util.tree.TreeUtilForCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateCategoryRequest {
    private String path;

    public String getParent() {
        return TreeUtilForCategory.getParentOfLeaf(this.path);
    }

    public String getName() {
        return TreeUtilForCategory.getLeafCategory(this.path);
    }
}
