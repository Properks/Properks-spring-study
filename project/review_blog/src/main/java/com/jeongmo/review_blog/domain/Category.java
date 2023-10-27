package com.jeongmo.review_blog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

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

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private Set<Category> children;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @Override
    public int compareTo(@NotNull Category o) {
        return this.name.compareTo(o.getName());
    }

    @Builder
    public Category(String name, Category parent) {
        this.name = name;
        children = new TreeSet<>();
        this.parent = parent;
        if (parent != null) {
            parent.addChild(this);
        }
    }

    public void addChild(Category child) {
        this.children.add(child);
    }
}
