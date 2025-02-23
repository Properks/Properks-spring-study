package com.example.customformlogin.global.auth.filter;

import com.example.customformlogin.domain.member.dto.request.MemberRequestDTO;
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
            MemberRequestDTO.LoginRequestDTO dto;
            try {
                String content = getBodyInRequest(request);
                dto = parseJson(content, MemberRequestDTO.LoginRequestDTO.class);
            } catch (IOException e) {
                throw new AuthenticationServiceException("Json Parsing Error In Json Filter");
            } catch (Exception e) {
                throw new AuthenticationServiceException("CustomJsonUsernamePasswordLoginFilter(" + e.getClass() + "): " + e.getMessage());
            }

            UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(dto.getUsername(), dto.getPassword());
            this.setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
        return null;
    }

    private String getBodyInRequest(HttpServletRequest request) throws IOException {
        return new String((new HttpServletRequestWrapper(request)).getInputStream().readAllBytes());
    }

    private <T> T parseJson(String content, Class<T> cls) throws IOException {
        ObjectMapper om = new ObjectMapper();
        return om.readValue(content, cls);
    }
}
