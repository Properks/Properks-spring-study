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
        try {
            FindArticleResponse savedArticle = new FindArticleResponse(articleService.createArticle(request));
            return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // GET
    @GetMapping("/api/article/{id}")
    public ResponseEntity<FindArticleResponse> getArticle(@PathVariable Long id) {
        try {
            FindArticleResponse foundArticle = new FindArticleResponse(articleService.getArticle(id));
            return ResponseEntity.ok().body(foundArticle);
        }
        catch(Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<FindArticleResponse>> getArticles() {
        try {
            List<FindArticleResponse> foundArticles = articleService.getArticles()
                    .stream().map(FindArticleResponse::new)
                    .toList();
            return ResponseEntity.ok().body(foundArticles);
        }
        catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/articles/category")
    public ResponseEntity<List<FindArticleResponse>> getArticlesWithCategory(@RequestParam Long categoryId) {
        try {
            List<FindArticleResponse> foundArticles = articleService.getArticlesByCategory(categoryId)
                    .stream()
                    .map(FindArticleResponse::new)
                    .toList();
            return ResponseEntity.ok().body(foundArticles);
        }
        catch(Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/articles/user")
    public ResponseEntity<List<FindArticleResponse>> getArticlesByUser(@RequestParam String nickname) {
        try {
            List<FindArticleResponse> foundArticle = articleService.getArticleByUser(nickname)
                    .stream()
                    .map(FindArticleResponse::new)
                    .toList();
            return ResponseEntity.ok().body(foundArticle);
        }
        catch(Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    //PUT
    @PutMapping("/api/article/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody UpdateArticleRequest request) {
        try {
            Article updatedArticle = articleService.updateArticle(id, request);
            return ResponseEntity.ok().body(updatedArticle);
        }
        catch(Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //DELETE
    @DeleteMapping("/api/article/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        try {
            articleService.deleteArticle(id);
            return ResponseEntity.ok().build();
        }
        catch(Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
