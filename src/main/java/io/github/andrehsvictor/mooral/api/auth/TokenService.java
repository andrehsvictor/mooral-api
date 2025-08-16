package io.github.andrehsvictor.mooral.api.auth;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final AuthenticationService authenticationService;

}
