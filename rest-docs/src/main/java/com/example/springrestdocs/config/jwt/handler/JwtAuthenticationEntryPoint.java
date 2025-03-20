package com.example.springrestdocs.config.jwt.handler;


import com.example.springrestdocs.apiPayload.ApiResponse;
import com.example.springrestdocs.apiPayload.code.base.FailureCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(401);
        ApiResponse<Object> errorResponse = ApiResponse.onFailure(
                FailureCode._UNAUTHORIZED.getReasonHttpStatus().getCode(),
                FailureCode._UNAUTHORIZED.getReasonHttpStatus().getMessage(),
                null
        );
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
}