package io.github.andrehsvictor.mooral.api.user.dto;

import jakarta.validation.constraints.Size;

/**
 * Request DTO for updating user profile information.
 * Only contains fields that can be safely updated by the user.
 */
public record UpdateUserRequest(
    
    @Size(max = 255, message = "First name must not exceed 255 characters")
    String firstName,
    
    @Size(max = 255, message = "Last name must not exceed 255 characters")
    String lastName,
    
    @Size(max = 255, message = "Avatar URL must not exceed 255 characters")
    String avatarUrl,
    
    String bio
) {}