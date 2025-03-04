package com.example.customformlogin.global.util;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private static final String BLACKLIST_PREFIX = "BLACK_";
    private static final String REFRESH_TOKEN_PREFIX = "REFRESH_";

    private final RedisTemplate<String, Object> redisTokenTemplate;


    public void addRefreshToken(UserDetails userDetails, String refresh, Duration duration) {
        redisTokenTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX + userDetails.getUsername(), refresh, duration);
    }

    public void addBlackList(String access, Duration duration) {
        redisTokenTemplate.opsForValue().set(BLACKLIST_PREFIX + access, true, duration);
    }

    public boolean isBlackList(String token) {
        return Boolean.TRUE.equals(redisTokenTemplate.hasKey(BLACKLIST_PREFIX + token));
    }

    public String getRefreshToken(UserDetails userDetails) {
        return this.getRefreshToken(userDetails.getUsername());
    }

    public String getRefreshToken(String username) {
        return (String) redisTokenTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + username);
    }

    public void deleteRefreshToken(UserDetails userDetails) {
        redisTokenTemplate.delete(REFRESH_TOKEN_PREFIX + userDetails.getUsername());
    }

    public void deleteBlackList(String access) {
        redisTokenTemplate.delete(BLACKLIST_PREFIX + access);
    }

}
