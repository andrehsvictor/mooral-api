package io.github.andrehsvictor.mooral.api.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import io.github.andrehsvictor.mooral.api.session.Session;
import io.github.andrehsvictor.mooral.api.session.SessionService;
import io.github.andrehsvictor.mooral.api.shared.dto.auth.CredentialsDto;
import io.github.andrehsvictor.mooral.api.shared.dto.auth.TokenPairDto;
import io.github.andrehsvictor.mooral.api.shared.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final SessionService sessionService;

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
}
