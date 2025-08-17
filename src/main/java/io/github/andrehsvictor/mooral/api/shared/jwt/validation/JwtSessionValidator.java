package io.github.andrehsvictor.mooral.api.shared.jwt.validation;

import java.util.UUID;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import io.github.andrehsvictor.mooral.api.session.SessionService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtSessionValidator implements OAuth2TokenValidator<Jwt> {

    private static final OAuth2Error INVALID_SESSION_ERROR = new OAuth2Error(
            OAuth2ErrorCodes.INVALID_TOKEN,
            "Session is invalid",
            null);

    private final SessionService sessionService;

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        String sessionId = token.getClaimAsString("sid");

        if (sessionId == null) {
            return OAuth2TokenValidatorResult.success();
        }

        boolean sessionExists = sessionService.existsById(UUID.fromString(sessionId));
        return sessionExists
                ? OAuth2TokenValidatorResult.success()
                : OAuth2TokenValidatorResult.failure(INVALID_SESSION_ERROR);
    }
}