package io.github.andrehsvictor.mooral.api.shared.jwt;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import io.github.andrehsvictor.mooral.api.session.Session;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtIssuanceService {

    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtProperties;

    public Jwt issueAccessToken(Session session) {
        Instant iss = Instant.now();
        Instant exp = iss.plus(jwtProperties.getAccessTokenLifespan());
        Instant nbf = iss;
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .subject(session.getUserId().toString())
                .issuer(jwtProperties.getIssuer())
                .audience(jwtProperties.getAudiences())
                .issuedAt(iss)
                .expiresAt(exp)
                .notBefore(nbf)
                .id(UUID.randomUUID().toString())
                .claim(jwtProperties.getAuthoritiesClaimName(), session.getScope())
                .claim("sid", session.getId().toString())
                .claim("typ", "Bearer")
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet));
    }

    public Jwt issueRefreshToken(Session session) {
        Instant iss = Instant.now();
        Instant exp = iss.plus(jwtProperties.getRefreshTokenLifespan());
        Instant nbf = iss;
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .subject(session.getUserId().toString())
                .issuer(jwtProperties.getIssuer())
                .audience(jwtProperties.getAudiences())
                .issuedAt(iss)
                .expiresAt(exp)
                .notBefore(nbf)
                .id(UUID.randomUUID().toString())
                .claim(jwtProperties.getAuthoritiesClaimName(), session.getScope())
                .claim("sid", session.getId().toString())
                .claim("typ", "Refresh")
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet));
    }

    public Jwt issueActionToken(UUID userId, String action, Duration lifespan, Map<String, Object> additionalClaims) {
        Instant iss = Instant.now();
        Instant exp = iss.plus(lifespan);
        Instant nbf = iss;
        JwtClaimsSet.Builder jwtClaimsSetBuilder = JwtClaimsSet.builder()
                .subject(userId.toString())
                .issuer(jwtProperties.getIssuer())
                .audience(jwtProperties.getAudiences())
                .issuedAt(iss)
                .expiresAt(exp)
                .notBefore(nbf)
                .id(UUID.randomUUID().toString())
                .claim("action", action)
                .claim("typ", "Action");
        if (additionalClaims != null) {
            additionalClaims.forEach(jwtClaimsSetBuilder::claim);
        }
        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSetBuilder.build()));
    }

    public Jwt issuePersonalAccessToken(UUID userId, String scope, Duration lifespan) {
        Instant iss = Instant.now();
        Instant exp = iss.plus(lifespan);
        Instant nbf = iss;
        JwtClaimsSet.Builder jwtClaimsSetBuilder = JwtClaimsSet.builder()
                .subject(userId.toString())
                .issuer(jwtProperties.getIssuer())
                .audience(jwtProperties.getAudiences())
                .issuedAt(iss)
                .notBefore(nbf)
                .id(UUID.randomUUID().toString())
                .claim("scope", scope)
                .claim("typ", "PAT");
        if (lifespan != null) {
            jwtClaimsSetBuilder.expiresAt(exp);
        }
        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSetBuilder.build()));
    }

}
