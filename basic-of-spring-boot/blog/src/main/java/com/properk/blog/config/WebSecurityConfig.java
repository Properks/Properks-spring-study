//package com.properk.blog.config;
//
//import com.properk.blog.service.UserDetailService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
//
//@RequiredArgsConstructor
//@Configuration
//public class WebSecurityConfig {
//
//    private final UserDetailService userService;
//    private static final String LOGIN = "/login";
//
//    // Deactivate spring security
//    @Bean
//    public WebSecurityCustomizer configure() {
//        return (web -> web.ignoring()
//                .requestMatchers(toH2Console())
//                .requestMatchers("/static/**"));
//    }
//
//    // Configure web-based security for specific http requests
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return http
//                .authorizeHttpRequests(authentication -> authentication
//                    .requestMatchers(LOGIN).permitAll()
//                    .requestMatchers("/signup").permitAll()
//                    .requestMatchers("/user").permitAll()
//                    .anyRequest().authenticated())
//                .formLogin(login -> login
//                    .loginPage(LOGIN)
//                    .defaultSuccessUrl("/articles"))
//                .logout(logout -> logout
//                    .logoutSuccessUrl(LOGIN)
//                    .invalidateHttpSession(true))
//                .build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder
//            , UserDetailService userDetailService) throws Exception {
//
//        AuthenticationManagerBuilder authenticationManagerBuilder =
//                http.getSharedObject(AuthenticationManagerBuilder.class);
//
//        authenticationManagerBuilder
//                .userDetailsService(userService)
//                .passwordEncoder(bCryptPasswordEncoder);
//
//        return authenticationManagerBuilder.build();
//    }
//
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
// Convert it to WebOAuthSecurityConfig.java