package com.example.springrestdocs.controller;

import com.example.springrestdocs.apiPayload.ApiResponse;
import com.example.springrestdocs.apiPayload.code.base.SuccessCode;
import com.example.springrestdocs.dto.ArticleRequest;
import com.example.springrestdocs.dto.ArticleResponse;
import com.example.springrestdocs.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<ApiResponse<ArticleResponse.CreatedArticleResponse>> createArticle(@Valid @RequestBody ArticleRequest.CreateArticleRequest dto) {
        ArticleResponse.CreatedArticleResponse response = articleService.createArticle(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.onSuccess(SuccessCode._CREATED, response)
        );
    }

    @GetMapping
    public ApiResponse<ArticleResponse.ArticleInfoListResponse> getArticles(@RequestParam(defaultValue = "1") int page,
                                                                        @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.onSuccess(articleService.getArticles(page, size));
    }

    @GetMapping("/{articleId}")
    public ApiResponse<ArticleResponse.ArticleInfoResponse> getArticle(@PathVariable Long articleId) {
        return ApiResponse.onSuccess(articleService.getArticles(articleId));
    }
}
