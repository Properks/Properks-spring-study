package com.jeongmo.revise_blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeongmo.revise_blog.domain.Article;
import com.jeongmo.revise_blog.dto.article_api.CreateArticleRequest;
import com.jeongmo.revise_blog.dto.article_api.UpdateArticleRequest;
import com.jeongmo.revise_blog.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ArticleApiControllerTest {

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    ArticleRepository articleRepository;

    @BeforeEach
    void initMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        articleRepository.deleteAll();
    }

    @Test
    @DisplayName("postArticle() : Success to create article")
    void postArticle() throws Exception{
        //given
        final String url = "/api/article";
        final String title = "title1";
        final String content = "content1";
        final CreateArticleRequest request = new CreateArticleRequest(title, content);
        final String requestBody = objectMapper.writeValueAsString(request);

        //when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        //then
        result.andExpect(status().isCreated());

        List<Article> articleList = articleRepository.findAll();

        assertThat(articleList).hasSize(1);
        assertThat(articleList.get(0).getTitle()).isEqualTo(request.getTitle());
        assertThat(articleList.get(0).getContent()).isEqualTo(request.getContent());
    }

    @DisplayName("getArticle() : Success to get an article")
    @Test
    void getArticle() throws Exception{
        //given
        final String url = "/api/article/{id}";
        final String title = "title1";
        final String content = "content1";
        final Article savedArticle = articleRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        //when
        ResultActions result = mockMvc.perform(get(url, savedArticle.getId()));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(savedArticle.getTitle()))
                .andExpect(jsonPath("$.content").value(savedArticle.getContent()));

    }

    @DisplayName("getArticles() : Success to get Articles")
    @Test
    void getArticles() throws Exception {
        //given
        final String url = "/api/articles";
        final Article savedArticle1 = articleRepository.save(Article.builder()
                .title("title1")
                .content("content1").build());
        final Article savedArticle2 = articleRepository.save(Article.builder()
                .title("title2")
                .content("content2").build());

        //when
        ResultActions result = mockMvc.perform(get(url));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value(savedArticle1.getTitle()))
                .andExpect(jsonPath("$[0].content").value(savedArticle1.getContent()))

                .andExpect(jsonPath("$[1].title").value(savedArticle2.getTitle()))
                .andExpect(jsonPath("$[1].content").value(savedArticle2.getContent()));
    }

    @DisplayName("deleteArticle() : Success to delete article")
    @Test
    void deleteArticle() throws Exception {
        //given
        final String url = "/api/article/{id}";
        final String title = "title";
        final String content = "content";
        final Article savedArticle = articleRepository.save(Article.builder().title(title).content(content).build());

        //when
        mockMvc.perform(delete(url, savedArticle.getId()))
                .andExpect(status().isOk());

        //then
        assertThat(articleRepository.findAll()).isEmpty();

    }

    @DisplayName("putArticle() : Success to update article")
    @Test
    void putArticle() throws Exception{
       //given
       final String url = "/api/article/{id}";
       final Article savedArticle =
               articleRepository.save(Article.builder().title("title").content("content").build());
       final UpdateArticleRequest request = new UpdateArticleRequest("updatedTitle", "updatedContent");
       final String requestBody = objectMapper.writeValueAsString(request);

       //when
        ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        //then
        result.andExpect(status().isOk());

        Article updatedArticle = articleRepository.findById(savedArticle.getId()).get();

        assertThat(updatedArticle.getTitle()).isEqualTo(request.getTitle());
        assertThat(updatedArticle.getContent()).isEqualTo(request.getContent());

    }
}