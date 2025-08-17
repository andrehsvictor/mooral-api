package io.github.andrehsvictor.mooral.api.shared.revokedtoken;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import io.github.andrehsvictor.mooral.api.session.SessionService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RevokedTokenService {

    private final RedisTemplate<String, Integer> redisTemplate;
    private final SessionService sessionService;

    private static final String REVOKED_TOKENS_KEY_PREFIX = "revoked:";

    public void revoke(Jwt jwt) {
        if (jwt.getClaimAsString("typ").equals("Refresh")) {
            String sessionId = jwt.getClaimAsString("sid");
            sessionService.invalidate(UUID.fromString(sessionId));
            return;
        }
        String tokenId = jwt.getId();
        Duration ttl = Duration.between(Instant.now(), jwt.getExpiresAt());
        redisTemplate.opsForValue().set(REVOKED_TOKENS_KEY_PREFIX + tokenId, 1, ttl);
    }

    public boolean isRevoked(Jwt jwt) {
        String tokenId = jwt.getId();
        return redisTemplate.hasKey(REVOKED_TOKENS_KEY_PREFIX + tokenId);
    }

}
