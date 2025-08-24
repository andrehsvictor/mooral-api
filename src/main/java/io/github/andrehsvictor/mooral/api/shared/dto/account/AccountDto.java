package io.github.andrehsvictor.mooral.api.shared.dto.account;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDto {
    private UUID id;
    private String name;
    private String username;
    private String email;
    private String bio;
    private String avatarUrl;
    private Instant createdAt;
    private Instant updatedAt;
    private String oauthProvider;
    private Set<AuthorityDto> authorities;
}
