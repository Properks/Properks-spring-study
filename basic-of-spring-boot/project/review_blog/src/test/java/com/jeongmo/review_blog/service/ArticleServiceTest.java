package com.jeongmo.review_blog.service;

import com.jeongmo.review_blog.domain.Article;
import com.jeongmo.review_blog.domain.Category;
import com.jeongmo.review_blog.domain.User;
import com.jeongmo.review_blog.dto.article_api.CreateArticleRequest;
import com.jeongmo.review_blog.dto.article_api.UpdateArticleRequest;
import com.jeongmo.review_blog.repository.ArticleRepository;
import com.jeongmo.review_blog.repository.CategoryRepository;
import com.jeongmo.review_blog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleServiceTest {

    @Autowired
    ArticleService articleService;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    User user;
    private static final String EMAIL = "test@email.com";
    private static final String PASSWORD = "test1234";
    private static final String NICKNAME = "Test#1234";

    @BeforeEach
    void init() {
        articleRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
        user = userRepository.save(User.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .build());

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
    }

    @Test
    void createArticle() {
        //given
        final String categoryName = "Category";
        final String childCategory = "childCategory";
        final String title = "title";
        final String content = "content";
        Category parent = categoryRepository.save(Category.builder()
                .name(categoryName)
                .build());
        Category category = categoryRepository.save(Category.builder()
                .name(childCategory)
                .parent(parent)
                .build());
        final String validCategoryPath = "Category_childCategory";

        CreateArticleRequest validRequest = new CreateArticleRequest(title, content, category.getId());
        CreateArticleRequest invalidCategory = new CreateArticleRequest(title, content, (long)-1);

        //when
        Article result = articleService.createArticle(validRequest);
        Exception resultWithInvalidParent = assertThrows(IllegalArgumentException.class,
                () -> articleService.createArticle(invalidCategory));

        //then
        assertThat(result.getTitle()).isEqualTo(title);
        assertThat(result.getContent()).isEqualTo(content);
        assertThat(result.getCategory().getId()).isEqualTo(category.getId());
        assertThat(result.getAuthor().getId()).isEqualTo(user.getId());

        assertThat(resultWithInvalidParent.getMessage()).isEqualTo("Invalid category path");

        SecurityContextHolder.clearContext();
        assertThrows(NullPointerException.class,
                () -> articleService.createArticle(validRequest));

    }

    @Test
    void getArticle() {
        //given
        final String categoryName = "Category";
        final String title = "title";
        final String content = "content";
        Category category = categoryRepository.save(Category.builder()
                .name(categoryName)
                .build());
        CreateArticleRequest create = new CreateArticleRequest(title, content, category.getId());
        Long articleId = articleRepository.save(Article.builder()
                .title(title)
                .content(content)
                .author(user)
                .category(category)
                .build()).getId();

        //when
        Article foundArticle = articleService.getArticle(articleId);

        //then
        assertThat(foundArticle.getTitle()).isEqualTo(title);
        assertThat(foundArticle.getContent()).isEqualTo(content);
        assertThat(foundArticle.getAuthor().getId()).isEqualTo(user.getId());
        assertThat(foundArticle.getCategory().getId()).isEqualTo(category.getId());

    }

    @Test
    void getArticles() {
        //given
        final String title1 = "title1";
        final String content1 = "content1";
        final String title2 = "title2";
        final String content2 = "content2";

        Category category = categoryRepository.save(Category.builder()
                .name("Category")
                .build());
        articleRepository.save(Article.builder()
                .title(title1)
                .content(content1)
                .author(user)
                .category(category)
                .build());
        articleRepository.save(Article.builder()
                .title(title2)
                .content(content2)
                .author(user)
                .category(category)
                .build());

        //when
        List<Article> result = articleService.getArticles();

        //then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo(title1);
        assertThat(result.get(0).getContent()).isEqualTo(content1);
        assertThat(result.get(0).getAuthor().getId()).isEqualTo(user.getId());
        assertThat(result.get(0).getCategory().getId()).isEqualTo(category.getId());

        assertThat(result.get(1).getTitle()).isEqualTo(title2);
        assertThat(result.get(1).getContent()).isEqualTo(content2);
        assertThat(result.get(1).getAuthor().getId()).isEqualTo(user.getId());
        assertThat(result.get(1).getCategory().getId()).isEqualTo(category.getId());

    }

    @Test
    void getArticlesByCategory() {
        //given
        final String categoryName = "Category";
        final String title1 = "title1";
        final String content1 = "content1";
        final String title2 = "title2";
        final String content2 = "content2";
        Category category = categoryRepository.save(Category.builder()
                .name(categoryName)
                .build());
        final Long categoryId = category.getId();
        articleRepository.save(Article.builder()
                .title(title1)
                .content(content1)
                .author(user)
                .category(category)
                .build());
        articleRepository.save(Article.builder()
                .title(title2)
                .content(content2)
                .author(user)
                .category(category)
                .build());

        //when
        List<Article> result = articleService.getArticlesByCategory(categoryId);

        //then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo(title1);
        assertThat(result.get(0).getContent()).isEqualTo(content1);
        assertThat(result.get(0).getAuthor().getId()).isEqualTo(user.getId());
        assertThat(result.get(0).getCategory().getId()).isEqualTo(categoryId);


        assertThat(result.get(1).getTitle()).isEqualTo(title2);
        assertThat(result.get(1).getContent()).isEqualTo(content2);
        assertThat(result.get(1).getAuthor().getId()).isEqualTo(user.getId());
        assertThat(result.get(1).getCategory().getId()).isEqualTo(categoryId);
    }

    @Test
    void getArticleByUser() {
        //given
        final String title1 = "title1";
        final String content1 = "content1";
        final String title2 = "title2";
        final String content2 = "content2";
        final String categoryName = "Category";
        Category category = categoryRepository.save(Category.builder()
                .name(categoryName)
                .build());
        articleRepository.save(Article.builder()
                .title(title1)
                .content(content1)
                .author(user)
                .category(category)
                .build());
        articleRepository.save(Article.builder()
                .title(title2)
                .content(content2)
                .author(user)
                .category(category)
                .build());

        //when
        List<Article> result = articleService.getArticleByUser(user.getId());

        //then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo(title1);
        assertThat(result.get(0).getContent()).isEqualTo(content1);
        assertThat(result.get(0).getAuthor().getId()).isEqualTo(user.getId());
        assertThat(result.get(0).getCategory().getId()).isEqualTo(category.getId());


        assertThat(result.get(1).getTitle()).isEqualTo(title2);
        assertThat(result.get(1).getContent()).isEqualTo(content2);
        assertThat(result.get(1).getAuthor().getId()).isEqualTo(user.getId());
        assertThat(result.get(1).getCategory().getId()).isEqualTo(category.getId());
    }

    @Test
    void updateArticle() {
        //given
        final String title1 = "title1";
        final String content1 = "content1";
        final String title2 = "title2";
        final String content2 = "content2";
        final String categoryName1 = "Category";
        final String categoryName2 = "Category2";
        Category category1 = categoryRepository.save(Category.builder()
                .name(categoryName1)
                .build());
        Category category2 = categoryRepository.save(Category.builder()
                .name(categoryName2)
                .build());
        Long articleId = articleRepository.save(Article.builder()
                .title(title1)
                .content(content1)
                .author(user)
                .category(category1)
                .build()).getId();
        UpdateArticleRequest request = new UpdateArticleRequest(title2, content2, category2.getId());

        //when
        Article result = articleService.updateArticle(articleId, request);

        //then
        assertThat(result.getTitle()).isEqualTo(title2);
        assertThat(result.getContent()).isEqualTo(content2);
        assertThat(result.getAuthor().getId()).isEqualTo(user.getId());
        assertThat(result.getCategory().getId()).isEqualTo(category2.getId());
        assertThat(result.getCategory().getName()).isEqualTo(categoryName2);
    }

    @Test
    void deleteArticle() {
        //given
        final String title1 = "title1";
        final String content1 = "content1";
        final String categoryName = "Category";
        Category category = categoryRepository.save(Category.builder()
                .name(categoryName)
                .build());
        Long articleId = articleRepository.save(Article.builder()
                .title(title1)
                .content(content1)
                .author(user)
                .category(category)
                .build()).getId();

        //when
        articleService.deleteArticle(articleId);

        //then
        List<Article> list = articleRepository.findAll();

        assertThat(list).isEmpty();

    }
}