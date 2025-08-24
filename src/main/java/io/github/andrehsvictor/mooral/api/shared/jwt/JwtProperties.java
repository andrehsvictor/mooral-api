package io.github.andrehsvictor.mooral.api.shared.jwt;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("io.github.andrehsvictor.mooral-api.jwt")
public class JwtProperties {

    private String issuer = "http://localhost:8080";

    @Value("${spring.security.oauth2.resourceserver.jwt.audiences}")
    private List<String> audiences = List.of("dev");

    @Value("${spring.security.oauth2.resourceserver.jwt.authorities-claim-name:scope}")
    private String authoritiesClaimName = "scope";

    private Lifespan lifespan = new Lifespan();

    @Data
    public static class Lifespan {
        private Duration accessToken = Duration.ofMinutes(15);
        private Duration refreshToken = Duration.ofHours(1);
        private ActionToken actionToken = new ActionToken();
    }

    @Data
    public static class ActionToken {
        private Duration emailVerification = Duration.ofHours(1);
        private Duration passwordReset = Duration.ofHours(1);
        private Duration emailChange = Duration.ofHours(1);
    }

    public Duration getAccessTokenLifespan() {
        return lifespan.getAccessToken();
    }

    public Duration getRefreshTokenLifespan() {
        return lifespan.getRefreshToken();
    }

    public Duration getActionTokenLifespan(String action) {
        return switch (action) {
            case "verify-email" -> lifespan.getActionToken().getEmailVerification();
            case "reset-password" -> lifespan.getActionToken().getPasswordReset();
            case "change-email" -> lifespan.getActionToken().getEmailChange();
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        };
    }

}
