package io.github.andrehsvictor.mooral.api.shared.jwt;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.stereotype.Component;

@Component
public class JwtClaimsBuilder {

    public JwtClaimsSet.Builder createBaseClaimsBuilder(String issuer, List<String> audience, String subject) {
        Instant now = Instant.now();
        return JwtClaimsSet.builder()
                .issuer(issuer)
                .audience(audience)
                .subject(subject)
                .issuedAt(now)
                .notBefore(now)
                .id(UUID.randomUUID().toString());
    }

    public JwtClaimsSet.Builder withTokenType(JwtClaimsSet.Builder builder, String tokenType) {
        return builder.claim("typ", tokenType);
    }

    public JwtClaimsSet.Builder withExpiration(JwtClaimsSet.Builder builder, Duration lifespan) {
        return builder.expiresAt(Instant.now().plus(lifespan));
    }

    public JwtClaimsSet.Builder withScope(JwtClaimsSet.Builder builder, String authorityClaimName, String scope) {
        return builder.claim(authorityClaimName, scope);
    }

    public JwtClaimsSet.Builder withSession(JwtClaimsSet.Builder builder, String sessionId) {
        return builder.claim("sid", sessionId);
    }

    public JwtClaimsSet.Builder withAction(JwtClaimsSet.Builder builder, String action) {
        return builder.claim("action", action);
    }

    public JwtClaimsSet.Builder withAdditionalClaims(JwtClaimsSet.Builder builder, Map<String, Object> additionalClaims) {
        if (additionalClaims != null) {
            additionalClaims.forEach(builder::claim);
        }
        return builder;
    }
}
