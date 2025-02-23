package com.example.customformlogin.global.config;

import com.example.customformlogin.global.auth.filter.CustomJsonUsernamePasswordLoginFilter;
import com.example.customformlogin.global.auth.filter.configurer.CustomJsonUsernamePasswordLoginFilterConfigurer;
import com.example.customformlogin.global.auth.handler.FormLoginFailureHandler;
import com.example.customformlogin.global.auth.handler.FormLoginSuccessfulHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final FormLoginSuccessfulHandler formLoginSuccessfulHandler;
    private final FormLoginFailureHandler formLoginFailureHandler;
    private final AuthenticationConfiguration authenticationConfiguration;

    private final String[] allowedUrl = {
            "/auth/signup",
            "/login",
            "/",
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers(allowedUrl).permitAll()
                        .anyRequest().authenticated()
//                        .anyRequest().permitAll()
                )
                .addFilterAt(customJsonUsernamePasswordLoginFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .formLogin(Customizer.withDefaults())
                .csrf(CsrfConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
        ;
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    // 안해도 스프링에서 UserDetailService와 Encoder를 세팅해주지만 명확하게 세팅해주기
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        authProvider.setHideUserNotFoundExceptions(false);
        return authProvider;
    }


    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CustomJsonUsernamePasswordLoginFilter customJsonUsernamePasswordLoginFilter(AuthenticationManager authenticationManager) {
        CustomJsonUsernamePasswordLoginFilterConfigurer configurer = new CustomJsonUsernamePasswordLoginFilterConfigurer();
        return configurer
                .authenticationManager(authenticationManager)
                .loginProcessingUrl("/login")
                .setUsernameParameter("username")
                .setPasswordParameter("password")
                .successHandler(formLoginSuccessfulHandler)
                .failureHandler(formLoginFailureHandler)
                .getFilter();
    }
}
