package io.github.andrehsvictor.mooral.api.shared.jwt.validation;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import io.github.andrehsvictor.mooral.api.shared.revokedtoken.RevokedTokenService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RevokedJwtValidator implements OAuth2TokenValidator<Jwt> {

    private final RevokedTokenService revokedTokenService;

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        if (revokedTokenService.isRevoked(token)) {
            return OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "Token is revoked", null));
        }
        return OAuth2TokenValidatorResult.success();
    }

}
