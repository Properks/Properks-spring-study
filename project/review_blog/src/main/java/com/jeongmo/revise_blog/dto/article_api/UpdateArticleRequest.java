package com.jeongmo.revise_blog.dto.article_api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateArticleRequest {

    private String title;
    private String content;
    private String author;
}
