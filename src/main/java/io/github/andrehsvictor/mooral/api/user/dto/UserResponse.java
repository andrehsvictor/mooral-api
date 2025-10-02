package io.github.andrehsvictor.mooral.api.user.dto;

import java.time.Instant;
import java.util.UUID;

/**
 * Response DTO for User entity.
 * Contains only safe, non-sensitive user information.
 */
public record UserResponse(
    UUID id,
    String username,
    String email,
    Boolean emailVerified,
    String firstName,
    String lastName,
    String fullName,
    String avatarUrl,
    String bio,
    Instant createdAt,
    Instant updatedAt
) {}