package org.jeongmo.springabstraction.auth.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.jeongmo.springabstraction.auth.filter.TokenAuthenticationFilter;
import org.jeongmo.springabstraction.auth.token.HeaderTokenExtractor;
import org.jeongmo.springabstraction.auth.token.JwtUtil;
import org.jeongmo.springabstraction.auth.token.TokenExtractor;
import org.jeongmo.springabstraction.domain.member.repository.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/members/sign-up", "/members/login").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(tokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
        ;
        return http.build();
    }

    @Bean
    Filter tokenFilter() {
        return new TokenAuthenticationFilter(jwtUtil, memberRepository, tokenExtractor());
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    TokenExtractor tokenExtractor() {
        return new HeaderTokenExtractor();
        // 쿠키 방식으로 변경할 경우 아래처럼만 바꾸어주면 된다.
        // 혹은 사용하는 Extractor의 위에 @Component 어노테이션을 없애고 사용하고자 하는 Extractor위에 해당 어노테이션을 추가해주면 된다.
        // return new CookieTokenExtractor();
    }

}
