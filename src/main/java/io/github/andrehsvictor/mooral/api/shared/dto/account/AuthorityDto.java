package io.github.andrehsvictor.mooral.api.shared.dto.account;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorityDto {
    private String name;
    private String displayName;
    private String description;
}
