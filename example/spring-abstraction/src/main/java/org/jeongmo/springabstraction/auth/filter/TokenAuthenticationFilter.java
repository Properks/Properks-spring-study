package org.jeongmo.springabstraction.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jeongmo.springabstraction.auth.token.TokenExtractor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public abstract class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenExtractor tokenExtractor;

    @Override
    protected final void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = tokenExtractor.extractToken(request);
        if (token != null) {
            try {
                Authentication authentication = attemptAuthentication(token);

                successAuthentication(authentication);
                filterChain.doFilter(request, response);
            } catch (Exception e) {
                handleException(response, e);
            }
        }
        else {
            filterChain.doFilter(request, response);
        }
    }

    protected abstract Authentication attemptAuthentication(String token) throws Exception;

    protected void successAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    protected void handleException(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(401);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), "인증 실패했습니다.");
    }
}
