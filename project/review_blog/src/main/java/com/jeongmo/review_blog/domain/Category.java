package com.jeongmo.review_blog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
    private Set<Category> children;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "category")
    private List<Article> articles;

    @Override
    public int compareTo(@NotNull Category o) {
        return this.name.compareTo(o.getName());
    }

    @Builder
    public Category(String name, Category parent) {
        this.name = name;
        this.children = new TreeSet<>();
        this.parent = parent;
        this.articles = new ArrayList<>();
        if (parent != null) {
            parent.addChild(this);
        }
    }

    public void addChild(Category child) {
        this.children.add(child);
    }

    public void addArticle(Article article) {
        this.articles.add(article);
    }
}
