package io.github.andrehsvictor.mooral.api.shared.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdatePasswordDto {

    @NotBlank(message = "Current password is required")
    @JsonProperty("current")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @JsonProperty("new")
    @Size(min = 8, max = 100, message = "New password must be between 8 and 100 characters")
    private String newPassword;

}
