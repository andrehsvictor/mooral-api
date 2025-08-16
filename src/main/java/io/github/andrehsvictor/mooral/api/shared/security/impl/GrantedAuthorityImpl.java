package io.github.andrehsvictor.mooral.api.shared.security.impl;

import org.springframework.security.core.GrantedAuthority;

import io.github.andrehsvictor.mooral.api.user.Authority;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GrantedAuthorityImpl implements GrantedAuthority {

    private static final long serialVersionUID = 2501181139236204944L;

    private final Authority authority;

    @Override
    public String getAuthority() {
        return authority.getName();
    }

    

}
