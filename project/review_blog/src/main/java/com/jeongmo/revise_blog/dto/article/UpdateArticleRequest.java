package com.jeongmo.revise_blog.dto.article;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateArticleRequest {

    private String title;
    private String content;
}
