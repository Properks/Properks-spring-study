package org.jeongmo.springabstraction.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jeongmo.springabstraction.auth.domain.CustomUserDetails;
import org.jeongmo.springabstraction.auth.token.JwtUtil;
import org.jeongmo.springabstraction.domain.member.entity.Member;
import org.jeongmo.springabstraction.domain.member.repository.MemberRepository;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractTokenInHeader(request);
        if (token != null) {
            try {
                Long userId = jwtUtil.getUserId(token);
                Member member = memberRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("사용자를 찾지 못했습니다."));

                UserDetails userDetails = new CustomUserDetails(member);
                Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } catch (Exception e) {
                handleException(response, e);
            }
        }
        else {
            filterChain.doFilter(request, response);
        }
    }

    private String extractTokenInHeader(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            return header.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    private String extractTokenInCookie(HttpServletRequest request) {
        String token = null;
        // 아래 end까지를 Util로 빼면서 더 refactoring 가능
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    token = cookie.getValue();
                }
            }
        }
        // end
        return token; // 쿠키의 경우 prefix를 보통 안 넣어서 바로 return 하는 방식, 만약 있다면 처리 후 반환
    }

    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(401);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), "인증 실패했습니다.");
    }
}
