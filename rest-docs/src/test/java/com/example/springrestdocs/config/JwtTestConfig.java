package com.example.springrestdocs.config;

import com.example.springrestdocs.config.jwt.filter.JwtFilter;
import com.example.springrestdocs.config.jwt.principal.PrincipalDetails;
import com.example.springrestdocs.config.jwt.principal.PrincipalDetailsService;
import com.example.springrestdocs.config.jwt.util.JwtProvider;
import com.example.springrestdocs.entity.Member;
import com.example.springrestdocs.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
public class JwtTestConfig {

    @Value("${Jwt.secret}")
    private String secret;

    @Value("${Jwt.token.access-expiration-time}")
    private long accessTokenExpiration;

    @Value("${Jwt.token.refresh-expiration-time}")
    private long refreshTokenExpiration;

    @Bean
    public JwtProvider jwtProvider() {
        MemberRepository memberRepository = mock(MemberRepository.class);
        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.of(Member.builder().id(1L).username("username").password("password").build()));

        return new JwtProvider(
                memberRepository,
                secret,
                accessTokenExpiration,
                refreshTokenExpiration
        );
    }

    @Bean
    public JwtFilter jwtFilter() {
        PrincipalDetailsService service = mock(PrincipalDetailsService.class);
        when(service.loadUserByUsername(anyString())).thenReturn(new PrincipalDetails(Member.builder().id(1L).username("username").password("password").build()));
        return new JwtFilter(jwtProvider(), service);
    }
}