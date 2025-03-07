package com.example.customformlogin.global.auth.filter;

import com.example.customformlogin.global.payload.CustomResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.naming.AuthenticationException;
import java.io.IOException;

@Slf4j
public abstract class AbstractTokenLogoutFilter extends OncePerRequestFilter {

    private final RequestMatcher requestMatcher = new AntPathRequestMatcher("/logout", "POST");
    private final LogoutSuccessHandler logoutSuccessHandler;
    private final String authorizationHeader;
    private final String tokenPrefix;

    protected AbstractTokenLogoutFilter(AbstractTokenFilter abstractTokenFilter, LogoutSuccessHandler logoutSuccessHandler) {
        this.authorizationHeader = abstractTokenFilter.getAuthorizationHeader();
        this.tokenPrefix = abstractTokenFilter.getTokenPrefix();
        this.logoutSuccessHandler = logoutSuccessHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (isLogoutRequest(request)) {
                String token = obtainToken(request);
                processLogout(request, response, token);
                logoutSuccessHandler.onLogoutSuccess(request, response, (Authentication) null);
            }
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            handleException(response, e);
        }
    }

    protected abstract void processLogout(HttpServletRequest request, HttpServletResponse response, String token);

    private boolean isLogoutRequest(HttpServletRequest request) {
        return requestMatcher.matches(request);
    }

    private String obtainToken(HttpServletRequest request) throws AuthenticationException {
        String header = request.getHeader(authorizationHeader);
        if (header == null || !header.startsWith(tokenPrefix)) {
            throw new AuthenticationException("토큰이 유효하지 않습니다.");
        }
        return header.substring(tokenPrefix.length());
    }

    private void handleException(HttpServletResponse response, Exception e) throws IOException{
        response.setStatus(HttpStatus.BAD_REQUEST.value());

        log.info("로그아웃 실패 ({}): {}", e.getClass(), e.getMessage());
        ObjectMapper om = new ObjectMapper();
        om.writeValue(response.getOutputStream(), CustomResponse.onFailure(HttpStatus.BAD_REQUEST, "로그아웃에 실패했습니다.", e.getMessage()));
    }
}
