package io.github.andrehsvictor.mooral.api.user;

import io.github.andrehsvictor.mooral.api.user.dto.CreateUserRequest;
import io.github.andrehsvictor.mooral.api.user.dto.UserResponse;
import io.github.andrehsvictor.mooral.api.user.dto.UpdateUserRequest;
import org.mapstruct.*;

/**
 * MapStruct mapper for User entity and DTOs.
 * Handles conversion between User entity and various DTOs.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    /**
     * Maps CreateUserRequest to User entity.
     * Excludes sensitive fields that should be set separately.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "emailVerificationToken", ignore = true)
    @Mapping(target = "emailVerificationTokenExpiresAt", ignore = true)
    @Mapping(target = "emailChangeToken", ignore = true)
    @Mapping(target = "emailChangeTokenExpiresAt", ignore = true)
    @Mapping(target = "emailChangeNewEmail", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "passwordResetToken", ignore = true)
    @Mapping(target = "passwordResetTokenExpiresAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(CreateUserRequest request);

    /**
     * Maps User entity to UserResponse DTO.
     * Excludes all sensitive information.
     */
    @Mapping(target = "fullName", expression = "java(user.getFullName())")
    UserResponse toResponse(User user);

    /**
     * Updates User entity from UpdateUserRequest.
     * Only updates non-null fields.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true) // Username shouldn't be updatable
    @Mapping(target = "email", ignore = true)    // Email changes require special flow
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "emailVerificationToken", ignore = true)
    @Mapping(target = "emailVerificationTokenExpiresAt", ignore = true)
    @Mapping(target = "emailChangeToken", ignore = true)
    @Mapping(target = "emailChangeTokenExpiresAt", ignore = true)
    @Mapping(target = "emailChangeNewEmail", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "passwordResetToken", ignore = true)
    @Mapping(target = "passwordResetTokenExpiresAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(UpdateUserRequest request, @MappingTarget User user);

    /**
     * Maps multiple User entities to UserResponse DTOs.
     */
    @IterableMapping(elementTargetType = UserResponse.class)
    java.util.List<UserResponse> toResponseList(java.util.List<User> users);
}