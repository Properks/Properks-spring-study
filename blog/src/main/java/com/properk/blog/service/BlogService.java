package com.properk.blog.service;

import com.properk.blog.domain.Article;
import com.properk.blog.dto.AddArticleRequest;
import com.properk.blog.dto.UpdateArticleRequest;
import com.properk.blog.repository.BlogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor // It is constructor injection
@Service
public class BlogService {

    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }

    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    public Article findById(long id) {
        return blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found" + id));
        // orElseThrow -> if Service can't found in database, throw Exception like this
    }

    public void delete(long id) {
        blogRepository.deleteById(id);
    }

    @Transactional // if this method has error while updating, cancel this method as if it wasn't worked.
    public Article update(long id,UpdateArticleRequest updateArticleRequest) {
        Article article = blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));
        article.update(updateArticleRequest.getTitle(), updateArticleRequest.getContent());
        return article;
    }
}
