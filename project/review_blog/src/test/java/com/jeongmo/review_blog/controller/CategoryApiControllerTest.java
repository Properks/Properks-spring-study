package com.jeongmo.review_blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeongmo.review_blog.domain.Category;
import com.jeongmo.review_blog.dto.category.CreateCategoryRequest;
import com.jeongmo.review_blog.repository.CategoryRepository;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryApiControllerTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("createCategory(): Success to create category")
    void createCategory() throws Exception{
        //given
        final String url = "/api/category";
        final String requestPath = "category";
        final CreateCategoryRequest request = new CreateCategoryRequest(requestPath);
        final String requestBody = objectMapper.writeValueAsString(request);

        //when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        //then
        result
                .andExpect(status().isCreated());

        List<Category> list = categoryRepository.findAll();
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getName()).isEqualTo(requestPath);

    }

    @Test
    @DisplayName("getCategories(): Get all categories")
    void getCategories() {

    }

    void getCategory() {

    }

    void deleteCategory() {

    }

    void updateCategory() {

    }
}