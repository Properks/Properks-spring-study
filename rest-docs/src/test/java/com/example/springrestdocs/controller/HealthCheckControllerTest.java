package com.example.springrestdocs.controller;

import com.example.springrestdocs.base.AbstractRestDocsTests;
import com.example.springrestdocs.service.HealthCheckService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HealthCheckController.class)
class HealthCheckControllerTest extends AbstractRestDocsTests {

    @MockitoBean
    HealthCheckService healthCheckService;

    @Test
    void healthCheck() throws Exception {
        // given
        given(healthCheckService.check())
                .willReturn("성공");

        // when
        ResultActions result = mockMvc.perform(get("/healthCheck")
//                .header(AUTHORIZATION_HEADER, token)
        );
        // then
        result
                .andExpect(status().isOk());
    }
}