package io.github.andrehsvictor.mooral.api.shared.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IntrospectTokenDto {

    @Pattern(message = "Token must be a valid JWT", regexp = "^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$")
    @NotBlank(message = "Token must not be blank")
    private String token;

}
