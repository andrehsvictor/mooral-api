package io.github.andrehsvictor.mooral.api.auth;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import io.github.andrehsvictor.mooral.api.session.Session;
import io.github.andrehsvictor.mooral.api.session.SessionService;
import io.github.andrehsvictor.mooral.api.shared.dto.auth.CredentialsDto;
import io.github.andrehsvictor.mooral.api.shared.dto.auth.RefreshTokenDto;
import io.github.andrehsvictor.mooral.api.shared.dto.auth.RevokeTokenDto;
import io.github.andrehsvictor.mooral.api.shared.dto.auth.TokenPairDto;
import io.github.andrehsvictor.mooral.api.shared.jwt.JwtService;
import io.github.andrehsvictor.mooral.api.shared.revokedtoken.RevokedTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtService jwtService;
    private final SessionService sessionService;
    private final RevokedTokenService revokedTokenService;
    private final AuthenticationService authenticationService;

    public TokenPairDto request(CredentialsDto credentialsDto, HttpServletRequest request) {
        Authentication authentication = authenticationService.authenticate(
                credentialsDto.getUsername(), credentialsDto.getPassword());
        Session session = sessionService.create(authentication, request);
        Jwt accessToken = jwtService.issueAccessToken(authentication, session.getId().toString());
        Jwt refreshToken = jwtService.issueRefreshToken(authentication, session.getId().toString());
        Long expiresIn = accessToken.getExpiresAt().getEpochSecond() - accessToken.getIssuedAt().getEpochSecond();
        String scope = accessToken.getClaimAsString("scope");
        return TokenPairDto.builder()
                .accessToken(accessToken.getTokenValue())
                .refreshToken(refreshToken.getTokenValue())
                .expiresIn(expiresIn)
                .scope(scope)
                .build();
    }

    public TokenPairDto refresh(RefreshTokenDto refreshTokenDto, HttpServletRequest request) {
        Jwt refreshToken = jwtService.decode(refreshTokenDto.getRefreshToken());
        Session session = sessionService.getById(UUID.fromString(refreshToken.getClaimAsString("sid")));
        revokedTokenService.revoke(refreshToken);
        Jwt accessToken = jwtService.issueAccessToken(refreshToken, session);
        Jwt newRefreshToken = jwtService.issueRefreshToken(refreshToken);
        Long expiresIn = accessToken.getExpiresAt().getEpochSecond() - accessToken.getIssuedAt().getEpochSecond();
        String scope = accessToken.getClaimAsString("scope");
        return TokenPairDto.builder()
                .accessToken(accessToken.getTokenValue())
                .refreshToken(newRefreshToken.getTokenValue())
                .expiresIn(expiresIn)
                .scope(scope)
                .build();
    }

    public void revoke(RevokeTokenDto revokeTokenDto) {
        Jwt token = jwtService.decode(revokeTokenDto.getToken());
        revokedTokenService.revoke(token);
        if (token.getClaimAsString("typ").equals("Refresh")) {
            sessionService.deleteById(UUID.fromString(token.getClaimAsString("sid")));
        }
    }
}
