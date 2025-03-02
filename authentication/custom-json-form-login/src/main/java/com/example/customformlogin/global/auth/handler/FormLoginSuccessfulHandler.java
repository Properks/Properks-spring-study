package com.example.customformlogin.global.auth.handler;

import com.example.customformlogin.domain.member.dto.response.MemberResponseDTO;
import com.example.customformlogin.global.auth.jwt.JwtUtil;
import com.example.customformlogin.global.payload.CustomResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class FormLoginSuccessfulHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails details = (UserDetails) authentication.getPrincipal();
        MemberResponseDTO.LoginResponseDTO responseDTO = MemberResponseDTO.LoginResponseDTO.builder()
                .accessToken(jwtUtil.createAccessToken(details))
                .refreshToken(jwtUtil.createRefreshToken(details))
                .build();
        log.info("로그인 성공 ({})", authentication.getName());
        ObjectMapper om = new ObjectMapper();
        om.writeValue(response.getOutputStream(), CustomResponse.onSuccess(responseDTO));
    }
}
