package io.github.andrehsvictor.mooral.api.shared.dto.user;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String id;
    private String name;
    private String username;
    private String bio;
    private String avatarUrl;
    private Integer boardsCount;
    private Integer boardsViewsCount;
    private Integer boardsLikesCount;
    private Integer boardsCommentsCount;
    private Instant createdAt;
}
