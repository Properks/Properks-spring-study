package com.properk.blog.dto;

import com.properk.blog.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddArticleRequest {

    private String title;
    private String content;

    public Article toEntity(String username) {
        return Article.builder()
                .author(username)
                .title(title)
                .content(content)
                .build();
    }
}
