package com.properk.blog.dto;

import com.properk.blog.domain.Article;
import lombok.Getter;

@Getter
public class ArticleListViewResponse {

    private final long id;
    private final String title;
    private final String content;

    public ArticleListViewResponse (Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}
