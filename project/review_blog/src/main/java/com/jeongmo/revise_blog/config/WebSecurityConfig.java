package com.jeongmo.revise_blog.config;

import com.jeongmo.revise_blog.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    private static final String LOGIN = "/login";
    private final CustomUserDetailService service;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/static/**"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(request ->
                        request.requestMatchers(LOGIN).permitAll()
                                .requestMatchers("/signup").permitAll()
                                .requestMatchers("/home").permitAll()
                                .anyRequest().authenticated())
                .formLogin(login ->
                        login.loginPage(LOGIN)
                                .defaultSuccessUrl("/home")
                                .usernameParameter("email")
                                .passwordParameter("password"))
                .logout(logout ->
                        logout.logoutUrl("/logout")
                                .logoutSuccessUrl(LOGIN)
                                .invalidateHttpSession(true))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);

        builder.userDetailsService(service)
                .passwordEncoder(passwordEncoder());

        return builder.build();

    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
