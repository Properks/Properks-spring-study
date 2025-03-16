package com.example.springrestdocs.service;

import com.example.springrestdocs.dto.ArticleRequest;
import com.example.springrestdocs.dto.ArticleResponse;
import com.example.springrestdocs.entity.Article;
import com.example.springrestdocs.exception.ArticleErrorCode;
import com.example.springrestdocs.exception.ArticleException;
import com.example.springrestdocs.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    @Override
    public ArticleResponse.CreatedArticleResponse createArticle(ArticleRequest.CreateArticleRequest dto) {
        Article article = articleRepository.save(dto.toEntity());
        return ArticleResponse.CreatedArticleResponse.toCreatedArticleResponse(article);
    }

    @Override
    public ArticleResponse.ArticleInfoListResponse getArticles(int page, int size) {
        return ArticleResponse.ArticleInfoListResponse.toArticleInfoListResponse(
                articleRepository.findAll(PageRequest.of(page - 1, size))
        );
    }

    @Override
    public ArticleResponse.ArticleInfoResponse getArticles(Long id) {
        return ArticleResponse.ArticleInfoResponse.toArticleInfoResponse(
                articleRepository.findById(id).orElseThrow(() -> new ArticleException(ArticleErrorCode.NOT_FOUND))
        );
    }
}
