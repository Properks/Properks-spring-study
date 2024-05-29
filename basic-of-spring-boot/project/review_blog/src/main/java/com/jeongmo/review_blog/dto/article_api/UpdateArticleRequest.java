package com.jeongmo.review_blog.dto.article_api;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The DTO to update article
 */
@AllArgsConstructor
@Getter
public class UpdateArticleRequest {

    /**
     * The new title
     */
    private String title;

    /**
     * The new content
     */
    private String content;

    /**
     * The new id of Category
     */
    private Long category;
}
