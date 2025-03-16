package com.example.springrestdocs.dto;

import com.example.springrestdocs.entity.Article;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class ArticleRequest {

    @Getter
    @AllArgsConstructor
    public static class CreateArticleRequest {
        @NotBlank
        private String title;
        @NotBlank
        private String content;

        public Article toEntity() {
            return Article.builder()
                    .title(this.title)
                    .content(this.content)
                    .build();
        }
    }

}
