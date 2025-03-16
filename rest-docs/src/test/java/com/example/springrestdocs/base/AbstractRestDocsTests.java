package com.example.springrestdocs.base;

import com.example.springrestdocs.config.JwtTestConfig;
import com.example.springrestdocs.config.RestDocsConfiguration;
import com.example.springrestdocs.config.SecurityTestConfig;
import com.example.springrestdocs.config.jwt.util.JwtProvider;
//import com.example.springrestdocs.entity.Member;
import jakarta.servlet.Filter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;

@Import({RestDocsConfiguration.class, JwtTestConfig.class, SecurityTestConfig.class})
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
@ActiveProfiles("test")
public abstract class AbstractRestDocsTests {

    protected static final String IDENTIFIER = "{class-name}/{method-name}";
    protected static final String AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @Autowired
    protected JwtProvider jwtProvider;

//    @Autowired
//    protected Filter filterChain;

    protected ResponseFieldsSnippet commonResponse;
    protected MockMvc mockMvc;
    protected String token;

    @BeforeEach
    void setUp(
            final WebApplicationContext context,
            final RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
//                .apply(SecurityMockMvcConfigurers.springSecurity(filterChain))
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(MockMvcResultHandlers.print())
                .alwaysDo(restDocs)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        setCommonResponse();
//        setToken();
    }

    private void setCommonResponse() {
        this.commonResponse = responseFields(
                fieldWithPath("isSuccess").description("성공 여부"),
                fieldWithPath("code").description("상태 코드"),
                fieldWithPath("message").description("상태 코드에 따른 메시지"),
                subsectionWithPath("result").description("결과 데이터")
        );
    }

//    private void setToken() {
//        this.token = "Bearer " + jwtProvider.createAccessToken(Member.builder().id(1L).username("username").password("password").build());
//    }

    protected final Attributes.Attribute defaultValue(String value) {
        return key("defaultValue").value(value);
    }

    protected final Attributes.Attribute required(boolean required) {
        return key("required").value(String.valueOf(required));
    }
}