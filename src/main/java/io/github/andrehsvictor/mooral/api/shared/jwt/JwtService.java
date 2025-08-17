package io.github.andrehsvictor.mooral.api.shared.jwt;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import io.github.andrehsvictor.mooral.api.shared.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final SecurityContextHolderStrategy securityContextHolderStrategy;

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
            throw new UnauthorizedException("Failed to decode JWT", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Jwt issueAccessToken(Authentication authentication, String sessionId) {
        String scope = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        Instant now = Instant.now();
        Instant expiresAt = now.plus(accessTokenLifespan);
        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .subject(authentication.getName())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .claim(authorityClaimName, scope)
                .claim("sid", sessionId)
                .id(UUID.randomUUID().toString())
                .claim("typ", "Bearer");
        return jwtEncoder.encode(JwtEncoderParameters.from(claimsBuilder.build()));
    }

    public Jwt issueRefreshToken(Authentication authentication, String sessionId) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(refreshTokenLifespan);
        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .subject(authentication.getName())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .claim("sid", sessionId)
                .id(UUID.randomUUID().toString())
                .claim("typ", "Refresh");
        return jwtEncoder.encode(JwtEncoderParameters.from(claimsBuilder.build()));
    }

    public Jwt issueActionToken(String subject, String action, Map<String, Object> additionalClaims) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(actionTokenLifespan);
        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .subject(subject)
                .issuedAt(now)
                .expiresAt(expiresAt)
                .claim("action", action)
                .id(UUID.randomUUID().toString())
                .claim("typ", "Action");
        additionalClaims.forEach(claimsBuilder::claim);
        return jwtEncoder.encode(JwtEncoderParameters.from(claimsBuilder.build()));
    }

    public Jwt issuePersonalAccessToken(String subject, Duration lifespan, String scope) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(lifespan);
        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .subject(subject)
                .issuedAt(now)
                .expiresAt(expiresAt)
                .claim(authorityClaimName, scope)
                .id(UUID.randomUUID().toString())
                .claim("typ", "PAT");
        return jwtEncoder.encode(JwtEncoderParameters.from(claimsBuilder.build()));
    }
}