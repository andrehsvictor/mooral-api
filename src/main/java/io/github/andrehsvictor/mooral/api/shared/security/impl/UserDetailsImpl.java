package io.github.andrehsvictor.mooral.api.shared.security.impl;

import java.time.Instant;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.github.andrehsvictor.mooral.api.user.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1492794089083710281L;

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities()
                .stream()
                .map(GrantedAuthorityImpl::new)
                .toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getId().toString();
    }

    @Override
    public boolean isEnabled() {
        return user.getEmailVerified();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getSuspendedUntil() == null || user.getSuspendedUntil().isBefore(Instant.now());
    }

}
