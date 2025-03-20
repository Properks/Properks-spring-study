package com.example.springrestdocs.config.jwt.filter;


import com.example.springrestdocs.apiPayload.ApiResponse;
import com.example.springrestdocs.apiPayload.code.BaseErrorCode;
import com.example.springrestdocs.apiPayload.exception.GeneralException;
import com.example.springrestdocs.config.jwt.principal.PrincipalDetailsService;
import com.example.springrestdocs.config.jwt.util.JwtProvider;
import com.example.springrestdocs.exception.MemberErrorCode;
import com.example.springrestdocs.exception.MemberException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final PrincipalDetailsService principalDetailsService;
    private final List<String> notJwtPaths = List.of(
            "/auth/login",
            "/auth/register",
            "/auth/oauth2/google",
            "/auth/oauth2/success"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestPath = request.getRequestURI();

        // JWT 검증 우회 경로 확인
        if (isNotJwtPath(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.split(" ")[1];
                String loginId = jwtProvider.getLoginId(token);
                UserDetails userDetails = principalDetailsService.loadUserByUsername(loginId);

                if (userDetails != null) {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
                            userDetails.getPassword(), userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new MemberException(MemberErrorCode.NOT_FOUND);
                }
            }

            filterChain.doFilter(request, response);
        } catch (GeneralException e) {
            BaseErrorCode code = e.getBaseErrorCode();
            response.setStatus(code.getReasonHttpStatus().getHttpStatus().value());
            response.setContentType("application/json; charset=UTF-8");

            ApiResponse<Object> customResponse = ApiResponse.onFailure(code.getReasonHttpStatus().getCode(),
                    code.getReasonHttpStatus().getMessage(), "");

            ObjectMapper om = new ObjectMapper();
            om.writeValue(response.getOutputStream(), customResponse);

        }
    }

    private boolean isNotJwtPath(String path) {
        return notJwtPaths.stream().anyMatch(path::startsWith);
    }
}