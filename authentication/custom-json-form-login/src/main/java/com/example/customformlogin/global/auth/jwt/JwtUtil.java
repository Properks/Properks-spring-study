package com.example.customformlogin.global.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final Duration accessExpiration;
    private final Duration refreshExpiration;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.time.access}") long accessExpiration,
                   @Value("${jwt.time.refresh}") long refreshExpiration) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpiration = Duration.ofMillis(accessExpiration);
        this.refreshExpiration = Duration.ofMillis(refreshExpiration);
    }

    public String createAccessToken(UserDetails details) {
        return createToken(details, accessExpiration);
    }

    public String createRefreshToken(UserDetails details) {
        return createToken(details, refreshExpiration);
    }

    public String getUsername(String token) {
        try {
            return getClaims(token).getPayload().getSubject();
        } catch (JwtException e) {
            return null;
        }
    }

    public boolean isValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private String createToken(UserDetails detail, Duration expiration) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(detail.getUsername())
                .claim("authorization", detail.getAuthorities())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expiration)))
                .signWith(secretKey)
                .compact();
    }

    private Jws<Claims> getClaims(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(secretKey)
                .clockSkewSeconds(60)
                .build()
                .parseSignedClaims(token);
    }

    public Duration getAccessExpiration() {
        return this.accessExpiration;
    }

    public Duration getRefreshExpiration() {
        return this.refreshExpiration;
    }
}
