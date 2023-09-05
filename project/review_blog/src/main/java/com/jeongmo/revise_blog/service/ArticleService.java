package com.jeongmo.revise_blog.service;

import com.jeongmo.revise_blog.domain.Article;
import com.jeongmo.revise_blog.dto.article_api.CreateArticleRequest;
import com.jeongmo.revise_blog.dto.article_api.UpdateArticleRequest;
import com.jeongmo.revise_blog.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    // Method create an new article
    public Article createArticle(@NotNull CreateArticleRequest request) {
        return articleRepository.save(request.toEntity());
    }

    // Method find an article
    public Article getArticle(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find Article"));
    }

    // Method find all article
    public List<Article> getArticles() {
        return articleRepository.findAll();
    }

    // Method update article
    @Transactional
    public Article updateArticle(Long id, UpdateArticleRequest request) {
        Article updatedArticle = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cannnot found Article for updating"));
        updatedArticle.update(request.getTitle(), request.getContent(), request.getAuthor());
        return updatedArticle;
    }

    // Method delete an article
    public void deleteArticle(Long id) {
        if (articleRepository.existsById(id)) {
            articleRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Cannot Found");
        }
    }
}
