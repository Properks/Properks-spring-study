package com.jeongmo.revise_blog.dto.article_view;

import com.jeongmo.revise_blog.domain.Article;
import com.jeongmo.revise_blog.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ArticleViewResponse {
    private Long id;
    private String title;
    private String content;
    private User author;

    public ArticleViewResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.author = article.getAuthor();
    }
}
