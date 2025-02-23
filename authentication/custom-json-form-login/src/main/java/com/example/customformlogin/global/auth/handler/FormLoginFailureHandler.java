package com.example.customformlogin.global.auth.handler;

import com.example.customformlogin.global.payload.CustomResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class FormLoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        CustomResponse<String> responseDTO = CustomResponse.onFailure(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다.", exception.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        log.info("로그인 실패 ({})", exception.getMessage());
        ObjectMapper om = new ObjectMapper();
        om.writeValue(response.getOutputStream(), responseDTO);
    }
}
