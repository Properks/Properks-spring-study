package com.jeongmo.revise_blog.controller;

import com.jeongmo.revise_blog.domain.Article;
import com.jeongmo.revise_blog.dto.CreateArticleRequest;
import com.jeongmo.revise_blog.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ArticleApiController {

    private final ArticleService articleService;

    @PostMapping("/api/article")
    public ResponseEntity<Article> postArticle(@RequestBody CreateArticleRequest request) {
        Article savedArticle = articleService.createArticle(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }
}
