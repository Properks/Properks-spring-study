package com.jeongmo.review_blog.dto.category;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class UpdateCategoryRequest {
    private Long categoryId;
    private Long newParent;
    private String newName;
}
