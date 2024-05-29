package com.jeongmo.review_blog.dto.category;

import com.jeongmo.review_blog.domain.Category;
import com.jeongmo.review_blog.util.tree.TreeUtilForCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED) // @NoArgsConstructor is needed at DTO and Entity
@AllArgsConstructor
@Getter
public class CreateCategoryRequest {
    private Long parentId;
    private String name;
}
