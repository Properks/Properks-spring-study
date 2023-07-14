package com.properk.blog.service;

import com.properk.blog.domain.RefreshToken;
import com.properk.blog.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenREpository;


    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenREpository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Not found refresh token"));
    }
}
