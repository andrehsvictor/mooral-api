package io.github.andrehsvictor.mooral.api.shared.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshTokenDto {

    @Pattern(message = "The refresh token must be a valid JWT", regexp = "^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$")
    @NotBlank(message = "The refresh token must be provided")
    private String refreshToken;
    
}
