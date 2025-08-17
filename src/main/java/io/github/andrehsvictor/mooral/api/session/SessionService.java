package io.github.andrehsvictor.mooral.api.session;

import java.time.Duration;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    @Value("${io.github.andrehsvictor.mooral-api.session.timeout:24h}")
    private Duration sessionTimeout = Duration.ofHours(24);

    public Session create(Authentication authentication, HttpServletRequest request) {
        String scope = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        Session session = Session.builder()
                .userId(UUID.fromString(authentication.getName()))
                .ip(request.getRemoteAddr())
                .userAgent(request.getHeader("User-Agent"))
                .scope(scope)
                .expiresAt(System.currentTimeMillis() + sessionTimeout.toMillis())
                .build();

        return sessionRepository.save(session);
    }

}
