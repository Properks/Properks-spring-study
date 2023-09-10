package com.jeongmo.revise_blog.dto.article_api;

import com.jeongmo.revise_blog.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateArticleRequest {
    private String title;
    private String content;
    private String author;

    public Article toEntity() {
        return Article.builder()
                .title(this.title)
                .content(this.content)
                .author(this.author)
                .build();
    }
}