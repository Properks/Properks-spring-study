package com.example.springrestdocs.repository;

import com.example.springrestdocs.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
