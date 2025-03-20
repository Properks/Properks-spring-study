package com.example.springrestdocs.config.jwt.util;

import com.example.springrestdocs.config.jwt.exception.AuthException;
import com.example.springrestdocs.config.jwt.exception.JwtErrorCode;
import com.example.springrestdocs.entity.Member;
import com.example.springrestdocs.exception.MemberErrorCode;
import com.example.springrestdocs.exception.MemberException;
import com.example.springrestdocs.repository.MemberRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class JwtProvider {

    private final MemberRepository memberRepository;
    private SecretKey secret;
    private long accessExpiration;
    private long refreshExpiration;

    public JwtProvider(MemberRepository memberRepository, @Value("${Jwt.secret}") String secret,
                       @Value("${Jwt.token.access-expiration-time}") long accessExpiration,
                       @Value("${Jwt.token.refresh-expiration-time}") long refreshExpiration) {
        this.secret = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
        this.memberRepository = memberRepository;
    }

    public String createAccessToken(Member member) {
        return createToken(member, this.accessExpiration);
    }

    public String createRefreshToken(Member member) {
        return createToken(member, this.refreshExpiration);
    }

    //공통 토큰 생성
    public String createToken(Member member, long expiration) {
        Instant issuedAt = Instant.now();
        Instant expiredAt = issuedAt.plusMillis(expiration);
        System.out.println("issuedAt: " + issuedAt);
        System.out.println("expiredAt: " + expiredAt);
        return Jwts.builder()
                .setHeader(Map.of("alg", "HS256", "typ", "JWT"))
                .setSubject(member.getUsername())
                .claim("id", member.getId())
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiredAt))
                .signWith(secret, SignatureAlgorithm.HS256)
                .compact();
    }

    //토큰 유효성 확인
    public boolean isValid(String token) {
        try {
            Jws<Claims> claims = getClaims(token);
            return claims.getBody().getExpiration().after(Date.from(Instant.now()));
        } catch (JwtException e) {
            log.error(e.getMessage());
            return false;
        } catch (Exception e) {
            log.error(e.getMessage() + ": 토큰이 유효하지 않습니다.");
            return false;
        }
    }

    //토큰의 클레임 가져오는 메서드
    public Jws<Claims> getClaims(String token) {
        try {
            return Jwts.parser()
                    .clockSkewSeconds(60)
                    .verifyWith(secret)
                    .build().parseSignedClaims(token);
        } catch (JwtException e) {
            log.error("JWT {} 오류: {}", e.getClass(), e.getMessage());
            throw new AuthException(JwtErrorCode.INVALID_TOKEN);
        }
    }

    //loginId 추출
    public String getLoginId(String token) {
        return getClaims(token).getBody().getSubject();
    }

    //토큰 만료 확인
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = getClaims(token);
            String username = claims.getPayload().getSubject();

            // 사용자 상태 확인
            Member member = memberRepository.findByUsername(username)
                    .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

            return !claims.getPayload().getExpiration().before(new Date());
        } catch (JwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

}
