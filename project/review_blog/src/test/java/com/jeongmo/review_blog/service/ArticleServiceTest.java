package com.jeongmo.review_blog.service;

import com.jeongmo.review_blog.domain.User;
import com.jeongmo.review_blog.repository.ArticleRepository;
import com.jeongmo.review_blog.repository.CategoryRepository;
import com.jeongmo.review_blog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleServiceTest {

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

        //when

        //then
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