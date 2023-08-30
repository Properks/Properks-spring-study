package com.jeongmo.revise_blog.service;

import com.jeongmo.revise_blog.domain.Article;
import com.jeongmo.revise_blog.dto.article.CreateArticleRequest;
import com.jeongmo.revise_blog.dto.article.FindArticleResponse;
import com.jeongmo.revise_blog.dto.article.UpdateArticleRequest;
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
    public FindArticleResponse getArticle(Long id) {
        return new FindArticleResponse(articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find Article")));
    }

    // Method find all article
    public List<FindArticleResponse> getArticles() {
        return articleRepository.findAll()
                .stream().map(FindArticleResponse::new)
                .toList();
    }

    // Method update article
    @Transactional
    public Article updateArticle(Long id, UpdateArticleRequest request) {
        Article updatedArticle = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cannnot found Article for updating"));
        updatedArticle.update(request.getTitle(), request.getContent());
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
