package com.jeongmo.review_blog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    /**
     * The title of article
     */
    @Column(name = "title")
    private String title;

    /**
     * The content of article
     */
    @Column(name = "content", columnDefinition = "MEDIUMTEXT")
    private String content;

    /**
     * The author of article
     * The author will be User form with @ManyToOne
     */
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * The constructor of Article
     *
     * @param title The title of your article
     * @param content The content of your article
     * @param author The author, which will be you
     * @param category The category
     */
    @Builder
    public Article(String title, String content, User author, Category category) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.category = category;
    }

    /**
     * The method to update article
     *
     * @param title The title which you will update
     * @param content The content which you will update
     */
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    /**
     * The method to update article ( + category)
     *
     * @param title The title which you will update
     * @param content The content which you will update
     * @param category The category which you will update
     */
    public void update(String title, String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }
}
