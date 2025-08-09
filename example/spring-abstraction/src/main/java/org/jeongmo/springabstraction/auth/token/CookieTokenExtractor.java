package org.jeongmo.springabstraction.auth.token;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookieTokenExtractor implements TokenExtractor {

    private static final String AUTHORIZATION_KEY = "accessToken";

    @Override
    public String extractToken(HttpServletRequest request) {
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_KEY)) {
                    token = cookie.getValue();
                }
            }
        }
        return token;
    }
}
