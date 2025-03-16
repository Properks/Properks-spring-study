package com.example.springrestdocs.service;

import com.example.springrestdocs.dto.ArticleRequest;
import com.example.springrestdocs.dto.ArticleResponse;

public interface ArticleService {
    ArticleResponse.CreatedArticleResponse createArticle(ArticleRequest.CreateArticleRequest dto);
    ArticleResponse.ArticleInfoListResponse getArticles(int page, int size);
    ArticleResponse.ArticleInfoResponse getArticles(Long id);

}
