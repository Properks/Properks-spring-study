package com.properk.blog.controller;

import com.properk.blog.domain.Article;
import com.properk.blog.dto.AddArticleRequest;
import com.properk.blog.dto.ArticleResponse;
import com.properk.blog.dto.UpdateArticleRequest;
import com.properk.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BlogApiController {

    private final BlogService blogService;

    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest article) {
        Article savedArticle = blogService.save(article);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        List<ArticleResponse> articleList = blogService.findAll()
                .stream()
                .map(ArticleResponse::new)
                .toList();
        return ResponseEntity.ok().body(articleList);

    }

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id) {
        ArticleResponse article = new ArticleResponse(blogService.findById(id));
        return ResponseEntity.ok().body(article);
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id) {
        blogService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(
            @PathVariable long id,
            @RequestBody UpdateArticleRequest updateArticleRequest
    ) {
        Article updatedArticle = blogService.update(id, updateArticleRequest);
        return ResponseEntity.ok().body(updatedArticle);

    }
}
