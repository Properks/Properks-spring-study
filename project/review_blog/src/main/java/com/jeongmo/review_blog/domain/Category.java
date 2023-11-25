package com.jeongmo.review_blog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category implements Comparable<Category> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * @// TODO: 2023/11/02 Make To be able to duplicate
     */
    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> children = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "category")
    private List<Article> articles = new ArrayList<>();

    @Override
    public int compareTo(@NotNull Category o) {
        return this.name.compareTo(o.getName());
    }

    public List<Category> getChildren() {
        return this.children.stream().sorted().toList();
    }

    @Builder
    public Category(String name, Category parent) {
        this.name = name;
        this.parent = parent;
    }

    public Category update(String name) {
        this.name = name;
        return this;
    }

    public void addChild(Category child) {
        this.children.add(child);
    }

    public void addArticle(Article article) {
        this.articles.add(article);
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public String getPath() {
        Category category = this;
        StringBuilder path = new StringBuilder(category.getName());
        while (category.getParent() != null) {
            path.insert(0, category.getParent().getName() + "_");
            category = category.getParent();
        }
        return path.toString();
    }
}
