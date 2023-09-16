package com.jeongmo.revise_blog.config;

import com.jeongmo.revise_blog.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;


import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toStaticResources;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableWebMvc
public class WebSecurityConfig {

    private static final String LOGIN = "/login";
    private final CustomUserDetailService service;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers(AntPathRequestMatcher.antMatcher("/static/**"))
        );
    }

    // TODO: Success to sign up but implement login and fix css application
    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector intro) {
        return new MvcRequestMatcher.Builder(intro);
    }
    // This function get HandlerMappingIntrospector from spring(?) and return MvcRequestMatcher.builder
    // I know reason why filterChain has HttpSecurity as parameter like this function

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector intro) throws Exception {
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
                            .invalidateHttpSession(true))
                .csrf(CsrfConfigurer<HttpSecurity>::disable);
        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);

        builder.userDetailsService(service)
                .passwordEncoder(passwordEncoder());

        return builder.build();

    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
