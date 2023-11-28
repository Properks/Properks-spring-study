package com.jeongmo.review_blog.service;

import com.jeongmo.review_blog.domain.Article;
import com.jeongmo.review_blog.domain.Category;
import com.jeongmo.review_blog.domain.User;
import com.jeongmo.review_blog.dto.article_api.CreateArticleRequest;
import com.jeongmo.review_blog.dto.article_api.UpdateArticleRequest;
import com.jeongmo.review_blog.repository.ArticleRepository;
import com.jeongmo.review_blog.util.tree.TreeUtilForCategory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryService categoryService;

    /**
     * Create article and save database
     *
     * @param request The information of article with CreateArticleRequest form
     * @return The saved article with article form.
     */
    public Article createArticle(@NotNull CreateArticleRequest request) {
        User author = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Category foundCategory;

        if (categoryService.isValid(request.getCategory())
                && categoryService.isExist(TreeUtilForCategory.getLeafCategory(request.getCategory()))) {
            // If it already exists, find it.
             foundCategory = categoryService.findCategory(TreeUtilForCategory.getLeafCategory(request.getCategory()));
        } else {
            throw new IllegalArgumentException("Invalid category path");
        }
        return articleRepository.save(request.toEntity(author, foundCategory));
    }

    /**
     * Find and get an article.
     *
     * @param id The id of article you want to find
     * @exception IllegalArgumentException When it can't find article
     * @return The found article
     */
    public Article getArticle(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find Article"));
    }

    /**
     * Find and get all articles
     *
     * @return The found articles with List form
     */
    public List<Article> getArticles() {
        return articleRepository.findAll();
    }

    public List<Article> getArticlesByCategory(Long id) {
        return articleRepository.getArticleByCategory_Id(id);
    }

    /**
     * Update article as new title and content
     *
     * @param id The id of article you will update
     * @param request The UpdateArticleRequest to update
     * @throws IllegalArgumentException If it can't find article
     * @return The updated article
     */
    @Transactional
    public Article updateArticle(Long id, UpdateArticleRequest request) {
        Article updatedArticle = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cannot found Article for updating"));
        updatedArticle.update(request.getTitle(), request.getContent());
        return updatedArticle;
    }

    /**
     * Delete an article
     *
     * @param id The id to be deleted
     * @throws IllegalArgumentException If it can't find article
     */
    public void deleteArticle(Long id) {
        if (articleRepository.existsById(id)) {
            articleRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Cannot Found");
        }
    }
}
