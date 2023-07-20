package com.properk.blog.config.oauth;

import com.properk.blog.config.jwt.TokenProvider;
import com.properk.blog.domain.RefreshToken;
import com.properk.blog.domain.User;
import com.properk.blog.repository.RefreshTokenRepository;
import com.properk.blog.service.UserService;
import com.properk.blog.util.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    private static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    private static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);
    private static final String REDIRECT_PATH = "/articles";

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestBasedOnCookieRepository;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2user = (OAuth2User) authentication.getPrincipal();
        User user = userService.findByEmail((String) oAuth2user.getAttribute("email"));

        // Make refresh token, save in repo and cookie
        String refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_DURATION);
        saveRefreshToken(user.getId(), refreshToken);
        addRefreshTokenToCookie(request, response, refreshToken);

        // Create Access token and add it to path
        String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);
        String uri = getTargetUrl(accessToken);

        // Remove all info related authentication
        clearAuthenticationAttribute(request, response);

        // redirect
        getRedirectStrategy().sendRedirect(request, response, uri);
    }

    private void saveRefreshToken(Long userId, String newRefreshToken) {
        refreshTokenRepository.findById(userId)
                .map(refreshToken -> refreshToken.update(newRefreshToken))
                .orElse(new RefreshToken(userId, newRefreshToken));
    }

    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response,
                                      String newRefreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, newRefreshToken, cookieMaxAge);
    }

    private void clearAuthenticationAttribute(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestBasedOnCookieRepository
                .removeAuthorizationRequestCookies(request, response);
    }

    private String getTargetUrl(String token) {
        return UriComponentsBuilder.fromUriString(REDIRECT_PATH)
                .queryParam("token", token)
                .build()
                .toUriString();
    }
}
