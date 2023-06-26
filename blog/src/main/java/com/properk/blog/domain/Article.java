package com.properk.blog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private long id;

    @Column(name = "title", updatable = false)
    private String title;

    @Column(name = "content", updatable = false)
    private String content;

    @Builder // Can use "Article.builder().title(String).content(String).build();" form for increased readability.
    public Article(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
