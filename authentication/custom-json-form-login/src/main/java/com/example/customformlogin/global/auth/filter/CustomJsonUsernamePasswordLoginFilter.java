package com.example.customformlogin.global.auth.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Map;

public class CustomJsonUsernamePasswordLoginFilter extends UsernamePasswordAuthenticationFilter {

    public CustomJsonUsernamePasswordLoginFilter() {
        super();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String contentType = request.getContentType();
        if (request.getMethod().equals(HttpMethod.POST.name()) && contentType != null && contentType.equals(MediaType.APPLICATION_JSON_VALUE)) {
            try {
                Map<String, String> requestBody = getBodyInRequest(request);
                String username = requestBody.get(getUsernameParameter());
                username = username != null ? username.trim() : "";
                String password = requestBody.get(getPasswordParameter());
                password = password != null ? password.trim() : "";

                UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
                this.setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);

            } catch (IOException e) {
                throw new AuthenticationServiceException("Json Parsing Error In Json Filter");
            } catch (Exception e) {
                throw new AuthenticationServiceException("CustomJsonUsernamePasswordLoginFilter(" + e.getClass() + "): " + e.getMessage());
            }
        }
        return null;
    }

    private Map<String, String> getBodyInRequest(HttpServletRequest request) throws IOException{
        String content = new String((new HttpServletRequestWrapper(request)).getInputStream().readAllBytes());
        ObjectMapper om = new ObjectMapper();
        return om.readValue(content, new TypeReference<>() {});
    }
}
