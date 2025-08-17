package io.github.andrehsvictor.mooral.api.shared.jwt;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import io.github.andrehsvictor.mooral.api.session.Session;
import io.github.andrehsvictor.mooral.api.shared.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtDecoder jwtDecoder;
    private final SecurityContextHolderStrategy securityContextHolderStrategy;
    private final JwtIssuanceService jwtIssuanceService;

    @Value("${spring.security.oauth2.resourceserver.jwt.authorities-claim-name:scope}")
    private String authorityClaimName = "scope";

    public UUID getCurrentUserUuid() {
        Authentication authentication = securityContextHolderStrategy.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            Jwt jwt = jwtAuthenticationToken.getToken();
            return UUID.fromString(jwt.getSubject());
        }
        return null;
    }

    public List<String> getCurrentUserAuthorities() {
        Authentication authentication = securityContextHolderStrategy.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            Jwt jwt = jwtAuthenticationToken.getToken();
            String[] scopes = jwt.getClaimAsString(authorityClaimName).split(" ");
            return List.of(scopes);
        }
        return List.of();
    }

    public Jwt decode(String token) {
        try {
            return jwtDecoder.decode(token);
        } catch (JwtException e) {
            throw new UnauthorizedException("Invalid JWT token: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while decoding JWT token", e);
        }
    }

    public Jwt issueAccessToken(Authentication authentication, String sessionId) {
        return jwtIssuanceService.issueAccessToken(authentication, sessionId);
    }

    public Jwt issueAccessToken(Jwt refreshToken, Session session) {
        return jwtIssuanceService.issueAccessTokenFromRefresh(refreshToken, session);
    }

    public Jwt issueRefreshToken(Authentication authentication, String sessionId) {
        return jwtIssuanceService.issueRefreshToken(authentication, sessionId);
    }

    public Jwt issueRefreshToken(Jwt refreshToken) {
        return jwtIssuanceService.issueRefreshTokenFromRefresh(refreshToken);
    }

    public Jwt issueActionToken(String subject, String action, Map<String, Object> additionalClaims) {
        return jwtIssuanceService.issueActionToken(subject, action, additionalClaims);
    }

    public Jwt issuePersonalAccessToken(String subject, Duration lifespan, String scope) {
        return jwtIssuanceService.issuePersonalAccessToken(subject, lifespan, scope);
    }

}