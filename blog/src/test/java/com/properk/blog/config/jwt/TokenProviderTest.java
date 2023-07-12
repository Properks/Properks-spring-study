package com.properk.blog.config.jwt;

import com.properk.blog.domain.User;
import com.properk.blog.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TokenProviderTest {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProperties jwtProperties;

    @DisplayName("generateToken() : Can make token with user info and expired time")
    @Test
    void generateToken() {
        //given
        final String userEmail = "test@email.com";
        final String userPassword = "test123";
        final Duration expiredAt = Duration.ofDays(14);
        User savedUser = userRepository.save(User.builder().email(userEmail).password(userPassword).build());

        //when
        String token = tokenProvider.generateToken(savedUser, expiredAt);

        //then
        Long userId = Jwts.parser()
                .setSigningKey(jwtProperties.getSecret())
                        .parseClaimsJws(token)
                                .getBody()
                .get("id", Long.class);
        assertThat(savedUser.getId()).isEqualTo(userId);
    }

    @DisplayName("validToken() : Check whether token is valid")
    @Test
    void validToken() {
        //given
        final Duration expiredAt = Duration.ofDays(14);
        User savedUser = User.builder().email("test@email.com").password("123").build();
        String token = JwtFactory.builder()
                .subject(savedUser.getEmail())
                .issuedAt(new Date())
                .expiredAt(new Date(new Date().getTime() + expiredAt.toMillis()))
                .build()
                .createToken(jwtProperties);

        //when
        boolean result = tokenProvider.validToken(token);

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("invalidToken() : Fail to verify token which is expired")
    @Test
    void invalidToken() {
        //given
        String token = JwtFactory.builder()
                .expiredAt(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build()
                .createToken(jwtProperties);

        //when
        boolean result = tokenProvider.validToken(token);

        //then
        assertThat(result).isFalse();
    }

    @DisplayName("getAuthentication() : Can get Authentication info with token")
    @Test
    void getAuthentication() {
        // given
        final String userEmail = "test@email.com";
        String token = JwtFactory.builder()
                .subject(userEmail)
                .expiredAt(new Date(new Date().getTime() + Duration.ofDays(7).toMillis()))
                .build()
                .createToken(jwtProperties);

        // when
        Authentication result = tokenProvider.getAuthentication(token);

        // then
        assertThat(((UserDetails) result.getPrincipal()).getUsername()).isEqualTo(userEmail);
    }

    @DisplayName("getUserId() : Can get user id with token")
    @Test
    void getUserId() {
        // given
        Long userId = 1L;
        String token = JwtFactory.builder()
                .expiredAt(new Date(new Date().getTime() + Duration.ofDays(7).toMillis()))
                .claims(Map.of("id", userId))
                .build()
                .createToken(jwtProperties);

        // when
        Long userIdByToken = tokenProvider.getUserId(token);

        // then
        assertThat(userIdByToken).isEqualTo(userId);
    }
}