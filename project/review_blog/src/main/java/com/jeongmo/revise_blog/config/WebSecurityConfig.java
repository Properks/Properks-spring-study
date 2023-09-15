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
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;


import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toStaticResources;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    private static final String LOGIN = "/login";
    private final CustomUserDetailService service;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers(toStaticResources().atCommonLocations())
        );
    }

    // TODO: Write explanation about mvc function and implement signup function and login function successfully
    @Bean
    public MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector intro) {
        return new MvcRequestMatcher.Builder(intro);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector intro) throws Exception {
        http
            .authorizeHttpRequests(request -> request
                    .requestMatchers(mvc(intro).pattern(LOGIN)).permitAll()
                    .requestMatchers(mvc(intro).pattern("/signup")).permitAll()
                    .requestMatchers(mvc(intro).pattern("/user")).permitAll()
                    .requestMatchers(mvc(intro).pattern("/home")).permitAll()
                    .anyRequest().authenticated())
            .formLogin(login ->
                    login.loginPage(LOGIN)
                            .defaultSuccessUrl("/home")
                            .usernameParameter("email")
                            .passwordParameter("password"))
            .logout(logout ->
                    logout.logoutUrl("/logout")
                            .logoutSuccessUrl(LOGIN)
                            .invalidateHttpSession(true));
        return http.build();
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
