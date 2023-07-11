package com.properk.blog.config.jwt;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@Getter
public class JwtFactory {

    private String subject = "test@email.com";
    private Date issuedAt = new Date();
    private Date expiredAt = new Date(new Date().getTime() + Duration.ofDays(14).toMillis());
    private Map<String, Object> claims = Collections.emptyMap();

    @Builder
    public JwtFactory(String subject,
                      Date issuedAt,
                      Date expiredAt,
                      Map<String, Object> claims) {
        this.subject = subject != null ? subject : this.subject;
        this.issuedAt = issuedAt != null ? issuedAt : this.issuedAt;
        this.expiredAt = expiredAt != null ? expiredAt : this.expiredAt;
        this.claims = claims != null ? claims : this.claims;
    }

//    public static JwtFactory withDefaultValues() {
//        return JwtFactory.builder().build();
//    }

    public String createToken(JwtProperties jwtProperties) {
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(this.issuedAt)
                .setExpiration(this.expiredAt)
                .setSubject(this.subject)
                .addClaims(this.claims)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
                .compact();
    }
}
