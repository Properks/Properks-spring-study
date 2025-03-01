package com.example.customformlogin.global.auth.filter;


import com.example.customformlogin.global.auth.jwt.JwtUtil;
import com.example.customformlogin.global.auth.principal.JwtTokenAuthenticationToken;
import com.example.customformlogin.global.auth.service.CustomDetailService;
import com.example.customformlogin.global.payload.CustomResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtTokenFilter extends AbstractTokenFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private final JwtUtil jwtUtil;
    private final CustomDetailService customDetailService;

    @Autowired
    public JwtTokenFilter(JwtUtil jwtUtil, CustomDetailService customDetailService) {
        super(AUTHORIZATION_HEADER, TOKEN_PREFIX);
        this.jwtUtil = jwtUtil;
        this.customDetailService = customDetailService;
    }

    @Override
    protected boolean validToken(String token) {
        return jwtUtil.isValid(token);
    }

    @Override
    protected Authentication createAuthentication(String token) throws AuthenticationException {
        String username = jwtUtil.getUsername(token);
        if (username == null) {
            throw new BadCredentialsException("토큰에서 사용자 정보를 찾을 수 없습니다.");
        }

        UserDetails userDetails = customDetailService.loadUserByUsername(username);
        return JwtTokenAuthenticationToken.authenticate(userDetails, token, userDetails.getAuthorities());
    }

    @Override
    protected void handleException(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        log.info("로그인 실패 ({}): {}", e.getClass(), e.getMessage());
        ObjectMapper om = new ObjectMapper();
        om.writeValue(response.getOutputStream(), CustomResponse.onFailure(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다.", e.getMessage()));
    }
}
