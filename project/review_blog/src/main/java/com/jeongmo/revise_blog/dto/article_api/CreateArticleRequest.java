package com.jeongmo.revise_blog.dto.article_api;

import com.jeongmo.revise_blog.domain.Article;
import com.jeongmo.revise_blog.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateArticleRequest {
    private String title;
    private String content;

    public Article toEntity(User author) {
        return Article.builder()
                .title(this.title)
                .content(this.content)
                .author(author)
                .build();
    }
}
