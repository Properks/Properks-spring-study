package com.example.customformlogin.global.auth.filter.configurer;

import com.example.customformlogin.global.auth.filter.CustomJsonUsernamePasswordLoginFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class CustomJsonUsernamePasswordLoginFilterConfigurer {

    private final CustomJsonUsernamePasswordLoginFilter filter;


    public CustomJsonUsernamePasswordLoginFilterConfigurer() {
        this.filter = new CustomJsonUsernamePasswordLoginFilter();
    }

    public CustomJsonUsernamePasswordLoginFilterConfigurer authenticationManager(AuthenticationManager authenticationManager) {
        filter.setAuthenticationManager(authenticationManager);
        return this;
    }

    public CustomJsonUsernamePasswordLoginFilterConfigurer successHandler(AuthenticationSuccessHandler authenticationSuccessHandler) {
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        return this;
    }

    public CustomJsonUsernamePasswordLoginFilterConfigurer failureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
        return this;
    }

    public CustomJsonUsernamePasswordLoginFilterConfigurer loginProcessingUrl(String loginProcessingUrl) {
        filter.setFilterProcessesUrl(loginProcessingUrl);
        return this;
    }

    public CustomJsonUsernamePasswordLoginFilterConfigurer setUsernameParameter(String usernameParameter) {
        filter.setPasswordParameter(usernameParameter);
        return this;
    }

    public CustomJsonUsernamePasswordLoginFilterConfigurer setPasswordParameter(String passwordParameter) {
        filter.setPasswordParameter(passwordParameter);
        return this;
    }

    public CustomJsonUsernamePasswordLoginFilter getFilter() {
        return this.filter;
    }
}