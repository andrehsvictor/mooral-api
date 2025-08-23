package io.github.andrehsvictor.mooral.api.auth;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.github.andrehsvictor.mooral.api.shared.dto.auth.CredentialsDto;
import io.github.andrehsvictor.mooral.api.shared.dto.auth.IntrospectTokenDto;
import io.github.andrehsvictor.mooral.api.shared.dto.auth.RefreshTokenDto;
import io.github.andrehsvictor.mooral.api.shared.dto.auth.RevokeTokenDto;
import io.github.andrehsvictor.mooral.api.shared.dto.auth.TokenPairDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final TokenService tokenService;

    @PostMapping("/api/v1/auth/token")
    public TokenPairDto request(@Valid @RequestBody CredentialsDto credentialsDto, HttpServletRequest request) {
        return tokenService.request(credentialsDto, request);
    }

    @PostMapping("/api/v1/auth/refresh")
    public TokenPairDto refresh(@Valid @RequestBody RefreshTokenDto refreshTokenDto, HttpServletRequest request) {
        return tokenService.refresh(refreshTokenDto, request);
    }

    @PostMapping("/api/v1/auth/revoke")
    public void revoke(@Valid @RequestBody RevokeTokenDto revokeTokenDto) {
        tokenService.revoke(revokeTokenDto);
    }

    @GetMapping("/api/v1/auth/introspect")
    public Jwt introspect(@Valid @RequestBody IntrospectTokenDto introspectTokenDto) {
        return tokenService.introspect(introspectTokenDto);
    }

}
