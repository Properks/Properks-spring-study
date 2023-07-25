package com.properk.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.properk.blog.domain.Article;
import com.properk.blog.domain.User;
import com.properk.blog.dto.AddArticleRequest;
import com.properk.blog.dto.UpdateArticleRequest;
import com.properk.blog.repository.BlogRepository;
import com.properk.blog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BlogApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach
    public void setMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        blogRepository.deleteAll();
    }

    @BeforeEach
    public void setSecurityContext() {
        userRepository.deleteAll();
        user = userRepository.save(User.builder()
                .email("test@email.com")
                .password("test")
                .build());

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(),user.getAuthorities())
        );
    }

    @DisplayName("addArticle : Success adding article")
    @Test
    public void addArticle() throws Exception{

        //given
        final String url = "/api/articles";
        final String title = "Title";
        final String content = "Content";
        final AddArticleRequest request = new AddArticleRequest(title, content);
        final String requestBody = objectMapper.writeValueAsString(request);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        //when
        ResultActions result = mockMvc.perform(post(url)
                        .principal(principal)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        //then
        result.andExpect(status().isCreated());

        List<Article> articleList= blogRepository.findAll();

        assertThat(articleList).hasSize(1);
        assertThat(articleList.get(0).getTitle()).isEqualTo(title);
        assertThat(articleList.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("searchArticleById() : Success searching article by id")
    @Test
    public void searchArticleById() throws Exception {
        //given
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";
        Article savedArticle = createDefaultArticle();

        //when
        final ResultActions result = mockMvc.perform(get(url, savedArticle.getId()));

        //then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.content").value(content));

    }

    @DisplayName("searchArticle() : Success finding article")
    @Test
    public void searchArticle() throws Exception{

        //given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";

        Article savedArticle = createDefaultArticle();

        //when
        ResultActions result = mockMvc.perform(get(url));

        //then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value(savedArticle.getTitle()))
                .andExpect(jsonPath("$[0].content").value(savedArticle.getContent()));
    }

    @DisplayName("deleteArticle() : Success deleting article")
    @Test
    public void deleteArticle() throws Exception{

        //given
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = createDefaultArticle();

        //when
        ResultActions result = mockMvc.perform(delete(url, savedArticle.getId()));

        //then
        result.andExpect(status().isOk());

        List<Article> articleList = blogRepository.findAll();
        assertThat(articleList).isEmpty();
    }

    @DisplayName("updateArticle() : Success updating article")
    @Test
    public void updateArticle() throws Exception{

        // given
        final String url = "/api/articles/{id}";
        final String title = "new title";
        final String content = "new content";
        Article savedArticle = createDefaultArticle();

        UpdateArticleRequest updateRequest = new UpdateArticleRequest(title, content);
        final String request = objectMapper.writeValueAsString(updateRequest);

        // when
        ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request));

        // then
        Article updatedArticle = blogRepository.findById(savedArticle.getId()).get();

        result.andExpect(status().isOk());

        assertThat(updatedArticle.getTitle()).isEqualTo(title);
        assertThat(updatedArticle.getContent()).isEqualTo(content);
    }

    private Article createDefaultArticle() {
        return blogRepository.save(Article.builder()
                .title("title")
                .content("content")
                .author(user.getUsername())
                .build());
    }
}