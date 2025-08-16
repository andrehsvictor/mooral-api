package io.github.andrehsvictor.mooral.api.shared.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CredentialsDto {

    @NotBlank(message = "Username cannot be blank")
    private String username; // or email

    @NotBlank(message = "Password cannot be blank")
    private String password;

}
