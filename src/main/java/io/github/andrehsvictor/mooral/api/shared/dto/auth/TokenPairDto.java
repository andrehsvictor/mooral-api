package io.github.andrehsvictor.mooral.api.shared.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenPairDto {
    private String accessToken;
    private String refreshToken;

    @Builder.Default
    private String type = "Bearer";
    
    private Long expiresIn;
}
