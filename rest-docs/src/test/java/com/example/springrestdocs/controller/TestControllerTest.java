package com.example.springrestdocs.controller;


import com.example.springrestdocs.base.AbstractRestDocsTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TestController.class)
class TestControllerTest extends AbstractRestDocsTests {

    @Test
    @DisplayName("Test API")
    void test() throws Exception {

        // given
        String response = "성공";

        // when
        ResultActions result = mockMvc.perform(get("/test")
//                .header(AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());
    }
}