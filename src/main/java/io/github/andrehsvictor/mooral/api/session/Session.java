package io.github.andrehsvictor.mooral.api.session;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("session")
public class Session implements Serializable {

    private static final long serialVersionUID = 593609038052669805L;

    @Id
    private UUID id;

    @Indexed
    private UUID userId;

    private String ip;
    private String userAgent;
    private List<String> authorities;

    @Builder.Default
    private Long lastActivity = System.currentTimeMillis();

    @Builder.Default
    private Long createdAt = System.currentTimeMillis();

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long expiresAt;

    public String getScope() {
        return String.join(" ", authorities);
    }

    public void updateLastActivity() {
        this.lastActivity = System.currentTimeMillis();
    }

}
