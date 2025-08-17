package io.github.andrehsvictor.mooral.api.shared.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RevokeTokenDto {

    @NotBlank(message = "Token must not be blank")
    private String token;
    
}
