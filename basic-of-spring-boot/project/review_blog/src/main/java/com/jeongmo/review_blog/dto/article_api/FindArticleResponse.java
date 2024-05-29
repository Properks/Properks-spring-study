package com.jeongmo.review_blog.dto.article_api;

import com.jeongmo.review_blog.domain.Article;
import com.jeongmo.review_blog.domain.Category;
import com.jeongmo.review_blog.domain.User;
import com.jeongmo.review_blog.dto.category.CategoryResponse;
import com.jeongmo.review_blog.dto.user.UserResponse;
import lombok.Getter;

/**
 * The DTO for response of found article.
 */
@Getter
public class FindArticleResponse {
    private final String title;
    private final String content;
    private final UserResponse author;
    private final CategoryResponse category;

    /**
     * Constructor
     *
     * @param article The article which you want to make as FindArticleResponse.
     */
    public FindArticleResponse(Article article) {
        this.title = article.getTitle();
        this.content = article.getContent();
        this.author = new UserResponse(article.getAuthor());
        this.category = new CategoryResponse(article.getCategory());
    }
}
