package com.jeongmo.review_blog.dto.article_view;

import com.jeongmo.review_blog.domain.Category;
import com.jeongmo.review_blog.util.tree.TreeUtilForCategory;
import lombok.Getter;

@Getter
public class CategoryResponse {

    private Long id;
    private String name;
    private Integer height;

    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.height = TreeUtilForCategory.getHeight(category);
    }

}
