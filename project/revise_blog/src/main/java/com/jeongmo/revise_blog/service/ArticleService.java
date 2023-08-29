package com.jeongmo.revise_blog.service;

import com.jeongmo.revise_blog.domain.Article;
import com.jeongmo.revise_blog.dto.CreateArticleRequest;
import com.jeongmo.revise_blog.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Article createArticle(@NotNull CreateArticleRequest request) {
        return articleRepository.save(request.toEntity());
    }
}
