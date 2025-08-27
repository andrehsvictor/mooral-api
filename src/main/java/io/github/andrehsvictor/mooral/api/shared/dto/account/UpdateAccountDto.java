package io.github.andrehsvictor.mooral.api.shared.dto.account;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateAccountDto {

    @Size(max = 100, message = "Username must be at most 100 characters")
    private String username;

    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

    @Size(max = 500, message = "Bio must be at most 500 characters")
    private String bio;

}
