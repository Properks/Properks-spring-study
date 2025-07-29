package com.example.springrestdocs.config;

import com.example.springrestdocs.config.jwt.filter.JwtFilter;
import com.example.springrestdocs.config.jwt.handler.JwtAuthenticationEntryPoint;
import com.example.springrestdocs.mock.TestFilterChain;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;


@TestConfiguration
public class SecurityTestConfig {

    private List<String> allowedUrl = List.of(
            "/allowed",
            "/article/*"
    );

    @Autowired
    JwtFilter jwtFilter;

    @Bean
    Filter filterChain() {
        return (servletRequest, servletResponse, filterChain) -> {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            if (allowedUrl.stream().noneMatch(url -> new AntPathRequestMatcher(url).matches(httpServletRequest))) {

                FilterChain customFilterChain = new TestFilterChain(jwtFilter);
                customFilterChain.doFilter(servletRequest, servletResponse);

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    jwtAuthenticationEntryPoint().commence(
                            (HttpServletRequest) servletRequest,
                            (HttpServletResponse) servletResponse,
                            new AuthenticationCredentialsNotFoundException("인증 실패")
                    );
                    return;
                }
            }
            filterChain.doFilter(servletRequest, servletResponse);
        };
    }

    @Bean
    AuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }
}