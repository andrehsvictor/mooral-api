package io.github.andrehsvictor.mooral.api.user;

import io.github.andrehsvictor.mooral.api.user.dto.CreateUserRequest;
import io.github.andrehsvictor.mooral.api.user.dto.UpdateUserRequest;
import io.github.andrehsvictor.mooral.api.user.dto.UserResponse;
import io.github.andrehsvictor.mooral.api.user.events.UserCreatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service layer for User operations.
 * Demonstrates MapStruct integration with Spring Modulith.
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    public UserService(
            UserRepository userRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Creates a new user from CreateUserRequest.
     * Demonstrates MapStruct usage for entity creation.
     */
    public UserResponse createUser(CreateUserRequest request) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Map DTO to entity using MapStruct
        User user = userMapper.toEntity(request);
        
        // Set password hash manually (sensitive operation)
        user.setPasswordHash(passwordEncoder.encode(request.password()));

        // Save user
        User savedUser = userRepository.save(user);

        // Publish domain event
        UserCreatedEvent event = new UserCreatedEvent(
            savedUser.getId(),
            savedUser.getUsername(),
            savedUser.getEmail(),
            savedUser.getFirstName(),
            savedUser.getLastName()
        );
        eventPublisher.publishEvent(event);

        // Return mapped response
        return userMapper.toResponse(savedUser);
    }

    /**
     * Updates user profile information.
     * Demonstrates MapStruct usage for entity updates.
     */
    public UserResponse updateUser(UUID userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Update entity using MapStruct (only non-null fields)
        userMapper.updateEntityFromRequest(request, user);

        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    /**
     * Finds user by ID and returns mapped response.
     */
    @Transactional(readOnly = true)
    public UserResponse findById(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        return userMapper.toResponse(user);
    }

    /**
     * Finds user by username and returns mapped response.
     */
    @Transactional(readOnly = true)
    public UserResponse findByUsername(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        return userMapper.toResponse(user);
    }

    /**
     * Search users and return paginated mapped responses.
     */
    @Transactional(readOnly = true)
    public Page<UserResponse> searchUsers(String searchTerm, Pageable pageable) {
        Page<User> users = userRepository.findBySearchTerm(searchTerm, pageable);
        
        // MapStruct automatically handles Page mapping
        return users.map(userMapper::toResponse);
    }

    /**
     * Verifies user email by token.
     */
    public UserResponse verifyEmail(String token) {
        User user = userRepository.findByValidEmailVerificationToken(token, java.time.Instant.now())
            .orElseThrow(() -> new IllegalArgumentException("Invalid or expired verification token"));

        user.verifyEmail();
        User savedUser = userRepository.save(user);
        
        return userMapper.toResponse(savedUser);
    }
}