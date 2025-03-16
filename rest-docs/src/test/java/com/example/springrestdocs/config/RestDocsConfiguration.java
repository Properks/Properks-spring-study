package com.example.springrestdocs.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

@TestConfiguration
public class RestDocsConfiguration {

    @Bean
    public RestDocumentationResultHandler write() {
        return MockMvcRestDocumentation.document(
                "{class-name}/{method-name}", // identifier
                preprocessRequest(prettyPrint(),
                        modifyHeaders()
                                .set("Authorization", "Bearer ${ACCESS_TOKEN}")
                                .remove("Content-Length")
                                .remove("Host")
                                .remove("accept")),
                preprocessResponse(prettyPrint(),
                        modifyHeaders()
                                .remove("Content-Length")),
                requestHeaders(
                        headerWithName("Authorization").description("Bearer ${ACCESS_TOKEN}")
                )
        );
    }

}
