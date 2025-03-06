package com.example.customformlogin.global.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Getter
public abstract class AbstractTokenFilter extends OncePerRequestFilter {

    private final String authorizationHeader;
    private final String tokenPrefix;
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();

    protected AbstractTokenFilter(String authorizationHeader, String tokenPrefix) {
        this.authorizationHeader = authorizationHeader;
        this.tokenPrefix = tokenPrefix;
    }

    @Override
    protected final void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            doFilterInternal(request, response);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            handleException(request, response, e);
        }
    }


    private void doFilterInternal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String header = request.getHeader(authorizationHeader);

        if (header != null && header.startsWith(tokenPrefix)) {
            String token = header.substring(tokenPrefix.length());

            try {
                if (!validToken(token)) {
                    throw new BadCredentialsException("토큰이 유효하지 않습니다.");
                }
                Authentication authentication = createAuthentication(token);
                successfulAuthentication(request, response, authentication);
            } catch (AuthenticationException e) {
                log.warn("인증 실패: {}", e.getMessage());
                throw e;
            }
        }
    }

    protected abstract boolean validToken(String token);

    protected abstract Authentication createAuthentication(String token) throws AuthenticationException;

    protected abstract void handleException(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException, ServletException;

    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        securityContextRepository.saveContext(context, request, response);
    }

    protected void setSecurityContextRepository(SecurityContextRepository repository) {
        this.securityContextRepository = repository;
    }

}
