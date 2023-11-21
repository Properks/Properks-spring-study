package com.jeongmo.review_blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeongmo.review_blog.domain.Category;
import com.jeongmo.review_blog.domain.User;
import com.jeongmo.review_blog.dto.category.CreateCategoryRequest;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach
    void init() {
        categoryRepository.deleteAll();
//        userRepository.deleteAll();

        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
//        user = userRepository.save(User.builder()
//                .email("test@email.com")
//                .password("test1234")
//                .nickname("test")
//                .build());
//
//        SecurityContextHolder.getContext().setAuthentication(
//                new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities())
//        );
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
    void getCategories() throws Exception{
        //given
        // another - another1
        // article - article1
        //         - article2
        // add it with order {article, article1, another, another1, article2}
        //             list: {another, another1, article, article1, article2}
        final String url = "/api/category";
        final String category1 = "article";
        final String category2 = "another";
        final String childName1 = "article1";
        final String childName2 = "article2";
        final String childName3 = "another1";
        Category parent1 = categoryRepository.save(Category.builder()
                .name(category1)
                .build());
        categoryRepository.save(Category.builder().name(childName1).parent(parent1).build());
        Category parent2 = categoryRepository.save(Category.builder()
                .name(category2)
                .build());
        categoryRepository.save(Category.builder().name(childName3).parent(parent2).build());
        categoryRepository.save(Category.builder().name(childName2).parent(parent1).build());

        //when
        ResultActions result = mockMvc.perform(get(url));

        //then
        parent1 = categoryRepository.findByName(category1).get();
        parent2 = categoryRepository.findByName(category2).get();
        Category child1 = categoryRepository.findByName(childName1).get();
        Category child2 = categoryRepository.findByName(childName2).get();
        Category child3 = categoryRepository.findByName(childName3).get();

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].id").value(parent2.getId()))
                .andExpect(jsonPath("$[0].name").value(parent2.getName()))
                .andExpect(jsonPath("$[1].id").value(child3.getId()))
                .andExpect(jsonPath("$[1].name").value(child3.getName()))
                .andExpect(jsonPath("$[2].id").value(parent1.getId()))
                .andExpect(jsonPath("$[2].name").value(parent1.getName()))
                .andExpect(jsonPath("$[3].id").value(child1.getId()))
                .andExpect(jsonPath("$[3].name").value(child1.getName()))
                .andExpect(jsonPath("$[4].id").value(child2.getId()))
                .andExpect(jsonPath("$[4].name").value(child2.getName()));
    }

    @Test
    @DisplayName("getCategory(): Success to get a category")
    void getCategory() throws Exception{
        //given
        final String url = "/api/category/{name}";
        final String categoryName = "category";
        final String childName = "child";
        Category parent = categoryRepository.save(Category.builder()
                .name(categoryName)
                .build());
        categoryRepository.save(Category.builder()
                .name(childName)
                .parent(parent)
                .build());

        //when
        ResultActions result = mockMvc.perform(get(url, categoryName));

        //then
        parent = categoryRepository.findByName(categoryName).get();
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(parent.getId()))
                .andExpect(jsonPath("$.name").value(parent.getName()))
                .andExpect(jsonPath("$.height").value(0));

        //when
        result = mockMvc.perform(get(url, childName));

        //then
        Category child = categoryRepository.findByName(childName).get();
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(child.getId()))
                .andExpect(jsonPath("$.name").value(child.getName()))
                .andExpect(jsonPath("$.height").value(1));

    }

    void deleteCategory() {

    }

    void updateCategory() {

    }
}