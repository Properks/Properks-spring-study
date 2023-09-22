package com.jeongmo.revise_blog.config;

import com.jeongmo.revise_blog.service.CustomUserDetailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;


import java.io.IOException;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    private static final String LOGIN = "/login";
    private static final String HOME = "/home";
    private final CustomUserDetailService service;

    @Bean
    WebSecurityCustomizer configure() {
        return (web -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers(AntPathRequestMatcher.antMatcher("/css/**")) // No "/static/**",
                .requestMatchers(AntPathRequestMatcher.antMatcher("/img/**"))
                .requestMatchers(AntPathRequestMatcher.antMatcher("/js/**"))
        );
    }

    // If i input @EnableWebMvc and didn't input WebMvcConfigurer(addResourceHandler), i can't get static resource

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector intro) {
        return new MvcRequestMatcher.Builder(intro);
    }
    // This function get HandlerMappingIntrospector from spring(?) and return MvcRequestMatcher.builder
    // I know reason why filterChain has HttpSecurity as parameter like this function

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http
            .authorizeHttpRequests(request -> request
                    .requestMatchers(mvc.pattern(LOGIN)).permitAll()
                    .requestMatchers(mvc.pattern("/signup")).permitAll()
                    .requestMatchers(mvc.pattern("/user")).permitAll()
                    .requestMatchers(mvc.pattern("/api/email/{email}")).permitAll() // Must input with {}
                    .requestMatchers(mvc.pattern(HOME)).permitAll()
                    .anyRequest().authenticated())
            .formLogin(login ->
                    login.loginPage(LOGIN)
                            .defaultSuccessUrl(HOME)
                            .failureHandler(failureHandler()))
            .logout(logout ->
                    logout.logoutUrl("/logout")
                            .logoutSuccessUrl(HOME)
                            .invalidateHttpSession(true))
                .csrf(CsrfConfigurer<HttpSecurity>::disable);
        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);

        builder.userDetailsService(service)
                .passwordEncoder(passwordEncoder);
        builder.eraseCredentials(false);

        return builder.build();

    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(service);
        provider.setPasswordEncoder(passwordEncoder());
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationFailureHandler failureHandler() {
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                String username = request.getParameter("username");
                String errorMsg;
                if (exception instanceof UsernameNotFoundException) {
                    errorMsg = username + " doesn't exist";
                }
                else if (exception instanceof BadCredentialsException) {
                    errorMsg = "Check your password";
                }
                else {
                    errorMsg = "Unknown Error";
                }

                response.sendRedirect("/login?error=" + errorMsg);
            }
        };
    }
}
