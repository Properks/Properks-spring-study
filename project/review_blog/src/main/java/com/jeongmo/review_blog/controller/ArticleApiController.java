package com.jeongmo.review_blog.controller;

import com.jeongmo.review_blog.domain.Article;
import com.jeongmo.review_blog.dto.article_api.CreateArticleRequest;
import com.jeongmo.review_blog.dto.article_api.FindArticleResponse;
import com.jeongmo.review_blog.dto.article_api.UpdateArticleRequest;
import com.jeongmo.review_blog.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleApiController {

    private final ArticleService articleService;

    // POST
    @PostMapping("/api/article")
    public ResponseEntity<FindArticleResponse> postArticle(@RequestBody CreateArticleRequest request) {
        FindArticleResponse savedArticle = new FindArticleResponse(articleService.createArticle(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    // GET
    @GetMapping("/api/article/{id}")
    public ResponseEntity<FindArticleResponse> getArticle(@PathVariable Long id) {
        FindArticleResponse foundArticle = new FindArticleResponse(articleService.getArticle(id));
        return ResponseEntity.ok().body(foundArticle);
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<FindArticleResponse>> getArticles() {
        List<FindArticleResponse> foundArticles = articleService.getArticles()
                .stream().map(FindArticleResponse::new)
                .toList();
        return ResponseEntity.ok().body(foundArticles);
    }

    @GetMapping("/api/articles/{categoryId}")
    public ResponseEntity<List<FindArticleResponse>> getArticlesWithCategory(@PathVariable Long categoryId) {
        List<FindArticleResponse> foundArticles = articleService.getArticlesByCategory(categoryId)
                .stream()
                .map(FindArticleResponse::new)
                .toList();
        return ResponseEntity.ok().body(foundArticles);
    }

    //PUT
    @PutMapping("/api/article/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody UpdateArticleRequest request) {
        Article updatedArticle = articleService.updateArticle(id, request);
        return ResponseEntity.ok().body(updatedArticle);
    }

    //DELETE
    @DeleteMapping("/api/article/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.ok().build();
    }
}
