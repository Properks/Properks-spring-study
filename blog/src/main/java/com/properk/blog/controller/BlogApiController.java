package com.properk.blog.controller;

import com.properk.blog.domain.Article;
import com.properk.blog.dto.AddArticleRequest;
import com.properk.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class BlogApiController {

    private final BlogService blogService;

    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest article) {
        Article savedArticle = blogService.save(article);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }
}
