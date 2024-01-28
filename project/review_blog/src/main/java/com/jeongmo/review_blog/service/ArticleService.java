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

import java.util.*;

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

        if (categoryService.isValid(request.getCategory())) {
            // If it already exists, find it.
             foundCategory = categoryService.findCategory(request.getCategory());
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

    public List<Article> getArticleByUser(String nickname) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (nickname.equals(user.getNickname())) {
            return articleRepository.getArticleByAuthor_Id(user.getId());
        }
        else {
            throw new IllegalArgumentException("Information of Nickname and Authentication is different");
        }
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
        if (categoryService.isValid(request.getCategory())) {
            throw new IllegalArgumentException("Invalid new category");
        }
        Category newCategory = categoryService.findCategory(request.getCategory());
        updatedArticle.update(request.getTitle(), request.getContent(), newCategory);
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

    /**
     * Delete Article by Id
     *
     * @param id The id of Author
     */
    @Transactional
    public void deleteArticlesByAuthorId(Long id) {
        articleRepository.deleteByAuthorId(id);
    }

    /**
     * Search in article
     *
     * @param categoryId The category id of article which you want to find
     * @param titleContent The keywords of title or content which you want to find (Containing)
     * @param nickname The nickname of article writer (Exactly same)
     * @param writer The nickname of article writer (Containing)
     */
    public List<Article> searchArticles(Long categoryId, String titleContent, String nickname, String writer) {
        List<Article> articles = getArticles();
        if (titleContent != null) {
            String[] keywords = titleContent.split(" ");
            Set<Article> result = new HashSet<>(); // Avoid to duplicate
            for (String keyword : keywords) { // Add article which have at least a keyword.to Set
                result.addAll(articleRepository.getArticleByTitleContainingOrContentContaining(keyword, keyword));
            }
            articles = new ArrayList<>(result);
        }
        else if (writer != null) {
            articles = articleRepository.getArticleByAuthor_NicknameContaining(writer);
        }
        if (nickname != null){
            articles = articles
                    .stream()
                    .filter(article -> article.getAuthor().getNickname().equals(nickname))
                    .toList();
        }
        if (categoryId != null) {
            articles = articles
                    .stream()
                    .filter(article -> article.getCategory().getId().equals(categoryId))
                    .toList();
        }
        return articles;
    }
}
