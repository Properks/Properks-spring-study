package com.example.springrestdocs.dto;

import com.example.springrestdocs.entity.Article;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

public class ArticleResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonPropertyOrder({"id", "title", "content"})
    public static class CreatedArticleResponse {
        @JsonProperty("id")
        private Long id;
        @JsonProperty("title")
        private String title;
        @JsonProperty("content")
        private String content;

        public static CreatedArticleResponse toCreatedArticleResponse(Article article) {
            return CreatedArticleResponse.builder()
                    .id(article.getId())
                    .title(article.getTitle())
                    .content(article.getContent())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ArticleInfoResponse {
        private Long id;
        private String title;
        private String content;

        public static ArticleInfoResponse toArticleInfoResponse(Article article) {
            return ArticleInfoResponse.builder()
                    .id(article.getId())
                    .title(article.getTitle())
                    .content(article.getContent())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ArticleInfoListResponse {
        private List<ArticleInfoResponse> items;
        private int page;
        private int totalPage;
        private int size;

        public static ArticleInfoListResponse toArticleInfoListResponse(Page<Article> page) {
            return ArticleInfoListResponse.builder()
                    .items(page.getContent().stream().map(ArticleInfoResponse::toArticleInfoResponse).toList())
                    .page(page.getNumber() + 1)
                    .totalPage(page.getTotalPages())
                    .size(page.getNumberOfElements())
                    .build();
        }
    }
}
