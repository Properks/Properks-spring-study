package com.example.customformlogin.global.auth.handler;

import com.example.customformlogin.domain.member.dto.response.MemberResponseDTO;
import com.example.customformlogin.global.payload.CustomResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class FormLoginSuccessfulHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        MemberResponseDTO.LoginResponseDTO responseDTO = MemberResponseDTO.LoginResponseDTO.builder()
                .accessToken("access")
                .refreshToken("refresh")
                .build();
        log.info("로그인 성공 ({})", authentication.getName());
        ObjectMapper om = new ObjectMapper();
        om.writeValue(response.getOutputStream(), CustomResponse.onSuccess(responseDTO));
    }
}
