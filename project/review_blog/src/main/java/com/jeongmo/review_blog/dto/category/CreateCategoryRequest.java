package com.jeongmo.review_blog.dto.category;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateCategoryRequest {
    private String name;
    private String parent;
}
