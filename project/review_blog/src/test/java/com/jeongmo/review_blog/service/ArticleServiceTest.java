package com.jeongmo.review_blog.service;

import com.jeongmo.review_blog.domain.Article;
import com.jeongmo.review_blog.domain.Category;
import com.jeongmo.review_blog.domain.User;
import com.jeongmo.review_blog.dto.article_api.CreateArticleRequest;
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
        categoryRepository.save(Category.builder()
                .name(childCategory)
                .parent(parent)
                .build());
        final String validCategoryPath = "Category_childCategory";
        final String invalidPathAtParent = "category_childCategory";
        final String invalidPathAtChild = "Category_child";

        CreateArticleRequest validRequest = new CreateArticleRequest(title, content, validCategoryPath);
        CreateArticleRequest invalidParent = new CreateArticleRequest(title, content, invalidPathAtParent);
        CreateArticleRequest invalidChild = new CreateArticleRequest(title, content, invalidPathAtChild);

        //when
        Article result = articleService.createArticle(validRequest);
        Exception resultWithInvalidParent = assertThrows(IllegalArgumentException.class,
                () -> articleService.createArticle(invalidParent));
        Exception resultWithInvalidChild = assertThrows(IllegalArgumentException.class,
                () -> articleService.createArticle(invalidChild));

        //then
        Category category = categoryRepository.findByName(childCategory).get();
        assertThat(result.getTitle()).isEqualTo(title);
        assertThat(result.getContent()).isEqualTo(content);
        assertThat(result.getCategory().getId()).isEqualTo(category.getId());
        assertThat(result.getAuthor().getId()).isEqualTo(user.getId());

        assertThat(resultWithInvalidParent.getMessage()).isEqualTo("Invalid category path");
        assertThat(resultWithInvalidChild.getMessage()).isEqualTo("Invalid category path");

        SecurityContextHolder.clearContext();
        assertThrows(NullPointerException.class,
                () -> articleService.createArticle(validRequest));

    }

    @Test
    void getArticle() {
        //given

        //when

        //then
    }

    @Test
    void getArticles() {
        //given

        //when

        //then
    }

    @Test
    void getArticlesByCategory() {
        //given

        //when

        //then
    }

    @Test
    void getArticleByUser() {
        //given

        //when

        //then
    }

    @Test
    void updateArticle() {
        //given

        //when

        //then
    }

    @Test
    void deleteArticle() {
        //given

        //when

        //then
    }
}