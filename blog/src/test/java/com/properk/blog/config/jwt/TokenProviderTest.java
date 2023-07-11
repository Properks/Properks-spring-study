package com.properk.blog.config.jwt;

import com.properk.blog.domain.User;
import com.properk.blog.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
    void generateToken() throws Exception {
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
}