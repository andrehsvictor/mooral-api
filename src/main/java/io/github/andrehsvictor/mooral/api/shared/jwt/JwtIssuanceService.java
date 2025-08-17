package io.github.andrehsvictor.mooral.api.shared.jwt;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import io.github.andrehsvictor.mooral.api.session.Session;
import io.github.andrehsvictor.mooral.api.shared.exception.BadRequestException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtIssuanceService {

    private final JwtEncoder jwtEncoder;
    private final JwtClaimsBuilder claimsBuilder;
    private final JwtScopeExtractor scopeExtractor;

    @Value("${io.github.andrehsvictor.mooral-api.jwt.access-token-lifespan:15m}")
    private Duration accessTokenLifespan = Duration.ofMinutes(15);

    @Value("${io.github.andrehsvictor.mooral-api.jwt.refresh-token-lifespan:1h}")
    private Duration refreshTokenLifespan = Duration.ofHours(1);

    @Value("${io.github.andrehsvictor.mooral-api.jwt.action-token-lifespan:1h}")
    private Duration actionTokenLifespan = Duration.ofHours(1);

    @Value("${spring.security.oauth2.resourceserver.jwt.authorities-claim-name:scope}")
    private String authorityClaimName = "scope";

    @Value("${spring.security.oauth2.resourceserver.jwt.audiences}")
    private List<String> audience = List.of("dev");

    @Value("${io.github.andrehsvictor.mooral-api.jwt.issuer:https://localhost:8080}")
    private String issuer = "https://localhost:8080";

    public Jwt issueAccessToken(Authentication authentication, String sessionId) {
        String scope = scopeExtractor.extractFromAuthentication(authentication);
        
        JwtClaimsSet.Builder builder = claimsBuilder.createBaseClaimsBuilder(issuer, audience, authentication.getName());
        claimsBuilder.withExpiration(builder, accessTokenLifespan);
        claimsBuilder.withTokenType(builder, "Bearer");
        claimsBuilder.withScope(builder, authorityClaimName, scope);
        claimsBuilder.withSession(builder, sessionId);
        
        return jwtEncoder.encode(JwtEncoderParameters.from(builder.build()));
    }

    public Jwt issueAccessTokenFromRefresh(Jwt refreshToken, Session session) {
        validateRefreshToken(refreshToken);
        
        JwtClaimsSet.Builder builder = claimsBuilder.createBaseClaimsBuilder(
            refreshToken.getIssuer().toString(), 
            refreshToken.getAudience(), 
            refreshToken.getSubject()
        );
        claimsBuilder.withExpiration(builder, accessTokenLifespan);
        claimsBuilder.withTokenType(builder, "Bearer");
        claimsBuilder.withScope(builder, authorityClaimName, session.getScope());
        claimsBuilder.withSession(builder, refreshToken.getClaimAsString("sid"));

        return jwtEncoder.encode(JwtEncoderParameters.from(builder.build()));
    }

    public Jwt issueRefreshToken(Authentication authentication, String sessionId) {
        JwtClaimsSet.Builder builder = claimsBuilder.createBaseClaimsBuilder(issuer, audience, authentication.getName());
        claimsBuilder.withExpiration(builder, refreshTokenLifespan);
        claimsBuilder.withTokenType(builder, "Refresh");
        claimsBuilder.withSession(builder, sessionId);

        return jwtEncoder.encode(JwtEncoderParameters.from(builder.build()));
    }

    public Jwt issueRefreshTokenFromRefresh(Jwt refreshToken) {
        validateRefreshToken(refreshToken);
        
        JwtClaimsSet.Builder builder = claimsBuilder.createBaseClaimsBuilder(
            refreshToken.getIssuer().toString(), 
            refreshToken.getAudience(), 
            refreshToken.getSubject()
        );
        claimsBuilder.withExpiration(builder, refreshTokenLifespan);
        claimsBuilder.withTokenType(builder, "Refresh");
        claimsBuilder.withSession(builder, refreshToken.getClaimAsString("sid"));

        return jwtEncoder.encode(JwtEncoderParameters.from(builder.build()));
    }

    public Jwt issueActionToken(String subject, String action, Map<String, Object> additionalClaims) {
        JwtClaimsSet.Builder builder = claimsBuilder.createBaseClaimsBuilder(issuer, audience, subject);
        claimsBuilder.withExpiration(builder, actionTokenLifespan);
        claimsBuilder.withTokenType(builder, "Action");
        claimsBuilder.withAction(builder, action);
        claimsBuilder.withAdditionalClaims(builder, additionalClaims);

        return jwtEncoder.encode(JwtEncoderParameters.from(builder.build()));
    }

    public Jwt issuePersonalAccessToken(String subject, Duration lifespan, String scope) {
        JwtClaimsSet.Builder builder = claimsBuilder.createBaseClaimsBuilder(issuer, audience, subject);
        claimsBuilder.withExpiration(builder, lifespan);
        claimsBuilder.withTokenType(builder, "PAT");
        claimsBuilder.withScope(builder, authorityClaimName, scope);

        return jwtEncoder.encode(JwtEncoderParameters.from(builder.build()));
    }

    private void validateRefreshToken(Jwt refreshToken) {
        if (!"Refresh".equals(refreshToken.getClaimAsString("typ"))) {
            throw new BadRequestException("The provided token is not a refresh token");
        }
    }
}
