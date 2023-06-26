package com.properk.blog.service;

import com.properk.blog.domain.Article;
import com.properk.blog.dto.AddArticleRequest;
import com.properk.blog.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor // It is constructor injection
@Service
public class BlogService {

    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }
}
