package com.example.springrestdocs.config.jwt.handler;


import com.example.springrestdocs.apiPayload.ApiResponse;
import com.example.springrestdocs.apiPayload.code.base.FailureCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(403);
        ApiResponse<Object> errorResponse = ApiResponse.onFailure(
                FailureCode._FORBIDDEN.getReasonHttpStatus().getCode(),
                FailureCode._FORBIDDEN.getReasonHttpStatus().getMessage(),
                null
        );
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
