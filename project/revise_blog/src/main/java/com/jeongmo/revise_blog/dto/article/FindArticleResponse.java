package com.jeongmo.revise_blog.dto.article;

import com.jeongmo.revise_blog.domain.Article;
import lombok.Getter;

@Getter
public class FindArticleResponse {
    private final String title;
    private final String content;

    public FindArticleResponse(Article article) {
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}
