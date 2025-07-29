package org.jeongmo.springabstraction.auth.config.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfigData {

    private String secret;
    private Time time;

    @Getter
    @Setter
    public static class Time {
        private long accessToken;
        private long refreshToken;
    }
}
