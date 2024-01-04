package com.jeongmo.review_blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeongmo.review_blog.domain.Article;
import com.jeongmo.review_blog.domain.Category;
import com.jeongmo.review_blog.domain.User;
import com.jeongmo.review_blog.dto.user.UpdateAccountNickname;
import com.jeongmo.review_blog.dto.user.UpdateAccountPassword;
import com.jeongmo.review_blog.repository.ArticleRepository;
import com.jeongmo.review_blog.repository.CategoryRepository;
import com.jeongmo.review_blog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserApiControllerTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    MockMvc mvc;

    @Autowired
    WebApplicationContext context;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    ObjectMapper mapper;


    User user;

    @BeforeEach
    void init() {
        articleRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
        user = userRepository.save(User.builder()
                .email("test@email.com")
                .password(encoder.encode("test1234"))
                .nickname("test#12345")
                .build());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities())
        );
    }


    @Test
    void deleteAccount() throws Exception{
        //given
        final String url = "/user/{userId}";
        Category category = categoryRepository.save(Category.builder().name("Category").build());
        articleRepository.save(Article.builder()
                .title("title")
                .content("content")
                .author(user)
                .category(category)
                .build());

        //when
        mvc.perform(delete(url, user.getId()));

        //then
        assertThat(articleRepository.findAll()).isEmpty();
        assertThat(userRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("updateNickname(): success to update nickname")
    void updateNickname() throws Exception{
        //given
        final String url = "/user/nickname";
        final String newNickname = "test1";
        final String code = "TEST";
        final Long id = user.getId();
        final String request = mapper.writeValueAsString(new UpdateAccountNickname(id, newNickname, code));

        //when
        ResultActions result = mvc.perform(put(url).contentType(MediaType.APPLICATION_JSON)
                .content(request));

        //then
        User updatedUser = userRepository.findById(id).get();
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(newNickname + "#" + code));
        assertThat(updatedUser.getNickname()).isEqualTo(newNickname + "#" + code);

    }

    @Test
    @DisplayName("updatePassword(): success to update password")
    void updatePassword() throws Exception {
        //given
        final String url = "/user/password";
        final Long id = user.getId();
        final String oldPassword = "test1234";
        final String newPassword = "TEST123456";
        final String request = mapper.writeValueAsString(new UpdateAccountPassword(id, oldPassword, newPassword));

        //when
        ResultActions result = mvc.perform(put(url).contentType(MediaType.APPLICATION_JSON)
                .content(request));

        //then
        result
                .andExpect(status().isOk());
        User updatedUser = userRepository.findById(id).get();
        assertThat(encoder.matches(newPassword, updatedUser.getPassword())).isTrue();
    }

    @Test
    void validPassword() throws Exception {
        //given
        final String url = "/api/password/{password}";
        final Long id = user.getId();
        final String validPassword = "test1234";
        final String invalidPassword = "TEST12345";

        //when
        ResultActions successResult = mvc.perform(post(url, validPassword));
        ResultActions failResult = mvc.perform(post(url, invalidPassword));

        //then
        successResult.andExpect(status().isOk());
        failResult.andExpect(status().isUnauthorized());
    }
}