package com.properk.blog.service;

import com.properk.blog.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final TokenProvider tokenProvider;

    public String createNewAccessToken(String refreshToken) {
        if (!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Not found refresh token");
        }
        Long userId = tokenProvider.getUserId(refreshToken);
        return tokenProvider.generateToken(userService.findById(userId), Duration.ofHours(2));
    }
}
