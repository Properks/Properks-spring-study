package com.example.springrestdocs.controller;

import com.example.springrestdocs.apiPayload.ApiResponse;
import com.example.springrestdocs.dto.ArticleRequest;
import com.example.springrestdocs.dto.ArticleResponse;
import com.example.springrestdocs.exception.ArticleErrorCode;
import com.example.springrestdocs.exception.ArticleException;
import com.example.springrestdocs.base.AbstractRestDocsTests;
import com.example.springrestdocs.service.ArticleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleController.class)
class ArticleControllerTest extends AbstractRestDocsTests {

    @MockitoBean
    ArticleService articleService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void createArticle() throws Exception {
        //given
        final String title = "title";
        final String content = "content";
        ArticleRequest.CreateArticleRequest dto = new ArticleRequest.CreateArticleRequest(title, content);
        given(articleService.createArticle(any(ArticleRequest.CreateArticleRequest.class)))
                .willReturn(new ArticleResponse.CreatedArticleResponse(1L, title, content));
        ApiResponse<ArticleResponse.CreatedArticleResponse> expectedResponse =
                ApiResponse.onSuccess(new ArticleResponse.CreatedArticleResponse(1L, title, content));

        //when
        ResultActions result = mockMvc.perform(post("/article")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        );

        //then
        result
                .andExpect(status().isCreated())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("content").description("게시글 내용")
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                fieldWithPath("id").description("게시글 ID"),
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("content").description("게시글 내용")
                        )
                ))
        ;

        verify(articleService).createArticle(any(ArticleRequest.CreateArticleRequest.class));
    }

    @Test
    void failToCreateArticleNoTitle() throws Exception {
        //given
        final String content = "content";
        ArticleRequest.CreateArticleRequest dto = new ArticleRequest.CreateArticleRequest(null, content);

        //when
        ResultActions result = mockMvc.perform(post("/article")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        );

        //then
        result
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    void failToCreateArticleNoContent() throws Exception {
        //given
        final String title = "title";
        ArticleRequest.CreateArticleRequest dto = new ArticleRequest.CreateArticleRequest(title, null);

        //when
        ResultActions result = mockMvc.perform(post("/article")
                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        );

        //then
        result
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    void getArticles() throws Exception {
        // given
        final int page = 1;
        final int size = 10;
        given(articleService.getArticles(any(Integer.class), any(Integer.class)))
                .willReturn(ArticleResponse.ArticleInfoListResponse.builder()
                        .items(List.of(
                                ArticleResponse.ArticleInfoResponse.builder().id(1L).title("title1").content("content1").build(),
                                ArticleResponse.ArticleInfoResponse.builder().id(2L).title("title2").content("content2").build()
                        ))
                        .page(page)
                        .totalPage(1)
                        .size(2)
                        .build()
                );

        // when
        ResultActions result = mockMvc.perform(get("/article")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .header(AUTHORIZATION_HEADER, token)
        );

        // then
        result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("page").description("페이지 번호").attributes(defaultValue("1")),
                                parameterWithName("size").description("페이지 크기").attributes(defaultValue("10"), required(false))
                        ),
                        commonResponse,
//                        getArticlesInfo(),
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                subsectionWithPath("items").description("데이터 배열").optional().type("ArticleInfoResponse[]"),
                                fieldWithPath("page").description("페이지 번호"),
                                fieldWithPath("totalPage").description("총 페이지 수"),
                                fieldWithPath("size").description("데이터 개수")
                        ),
                        responseFields(
                                beneathPath("result.items").withSubsectionId("result.items"),
                                fieldWithPath("id").description("게시글 ID"),
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("content").description("게시글 내용")
                        )
                ));
    }

    private ResponseFieldsSnippet getArticlesInfo() {
        return responseFields(
                beneathPath("result").withSubsectionId("result"),
                subsectionWithPath("items").description("데이터 배열").optional().type("ArticleInfoResponse[]"),
                fieldWithPath("page").description("페이지 번호"),
                fieldWithPath("totalPage").description("총 페이지 수"),
                fieldWithPath("size").description("데이터 개수")
        );
    }

    @Test
    void getArticle() throws Exception {
        // given
        final Long  pathVariable = 1L;
        given(articleService.getArticles(any(Long.class)))
                .willReturn(ArticleResponse.ArticleInfoResponse.builder()
                        .id(1L)
                        .title("title")
                        .content("content")
                        .build()
                );

        // when
        ResultActions result = mockMvc.perform(get("/article/{articleId}", pathVariable)
                .header(AUTHORIZATION_HEADER, token));


        //then
        result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("articleId").description("게시글 ID")
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                fieldWithPath("id").description("게시글 ID"),
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("content").description("게시글 내용")
                        )
                ));
    }

    @Test
    void failToFindArticle() throws Exception {
        // given
        final Long pathVariable = 1L;
        given(articleService.getArticles(any(Long.class)))
                .willThrow(new ArticleException(ArticleErrorCode.NOT_FOUND));

        // when
        ResultActions result = mockMvc.perform(get("/article/{articleId}", pathVariable)
                );

        // then
        result
                .andExpect(status().isNotFound());
    }

}