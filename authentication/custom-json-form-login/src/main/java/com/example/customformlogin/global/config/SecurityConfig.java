package com.example.customformlogin.global.config;

import com.example.customformlogin.global.auth.filter.CustomJsonUsernamePasswordLoginFilter;
import com.example.customformlogin.global.auth.filter.JwtTokenFilter;
import com.example.customformlogin.global.auth.filter.JwtTokenLogoutFilter;
import com.example.customformlogin.global.auth.filter.configurer.CustomJsonUsernamePasswordLoginFilterConfigurer;
import com.example.customformlogin.global.auth.handler.FormLoginFailureHandler;
import com.example.customformlogin.global.auth.handler.FormLoginSuccessfulHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final FormLoginSuccessfulHandler formLoginSuccessfulHandler;
    private final FormLoginFailureHandler formLoginFailureHandler;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtTokenFilter jwtTokenFilter;
    private final JwtTokenLogoutFilter jwtTokenLogoutFilter;

    private final String[] allowedUrl = {
            "/auth/signup",
            "/login",
            "/home",
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
                .formLogin(FormLoginConfigurer::disable)
                .addFilterAt(customJsonUsernamePasswordLoginFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtTokenLogoutFilter, LogoutFilter.class)
                .addFilterBefore(jwtTokenFilter, JwtTokenLogoutFilter.class)
//                .securityContext(context -> context.securityContextRepository(jwtTokenFilter.getSecurityContextRepository()))
                // 세션 생성 전략은 필요시에만
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                // SecurityContextHolderFilter의 repository를 HttpSessionSecurityContextRepository로 설정하여 CustomLoginFilter에서 저장한 SecurityContext 정보를 잘 불러올 수 있도록 설정
//                .securityContext(context -> context.securityContextRepository(new HttpSessionSecurityContextRepository()))
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
                // 기존은 RequestAttributeSecurityContextRepository로 요청마다 SecurityContext를 관리, HttpSessionSecurityContextRepository로 변경하여 세션별로 관리하도록 변경
//                .setSecurityContextRepository(new HttpSessionSecurityContextRepository())
                .successHandler(formLoginSuccessfulHandler)
//                .setLoginSuccessfulUrl("/home")
                .failureHandler(formLoginFailureHandler)
                .getFilter();
    }
}
