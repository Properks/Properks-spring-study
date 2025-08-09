package org.jeongmo.springabstraction.auth.token;

import jakarta.servlet.http.HttpServletRequest;

public interface TokenExtractor {
    String extractToken(HttpServletRequest request);
}
