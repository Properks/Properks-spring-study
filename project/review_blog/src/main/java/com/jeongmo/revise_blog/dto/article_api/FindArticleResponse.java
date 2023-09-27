package com.jeongmo.revise_blog.dto.article_api;

import com.jeongmo.revise_blog.domain.Article;
import com.jeongmo.revise_blog.domain.User;
import lombok.Getter;

@Getter
public class FindArticleResponse {
    private final String title;
    private final String content;
    private final User author;

    public FindArticleResponse(Article article) {
        this.title = article.getTitle();
        this.content = article.getContent();
        this.author = article.getAuthor();
    }
}
