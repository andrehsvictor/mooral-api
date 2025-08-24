package io.github.andrehsvictor.mooral.api.shared.dto.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateAvatarDto {

    @NotBlank(message = "Avatar URL is required")
    @Pattern(regexp = "^(http|https)://.*$", message = "Invalid URL format")
    private String avatarUrl;

}
