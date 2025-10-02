package io.github.andrehsvictor.mooral.api.user.events;

import io.github.andrehsvictor.mooral.api.shared.domain.events.DomainEvent;

import java.util.UUID;

/**
 * Event published when a new user is created in the system.
 * Can be consumed by other modules for welcome emails, analytics, etc.
 */
public class UserCreatedEvent extends DomainEvent {
    
    private final UUID userId;
    private final String username;
    private final String email;
    private final String firstName;
    private final String lastName;
    
    public UserCreatedEvent(UUID userId, String username, String email, String firstName, String lastName) {
        super();
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public UUID getUserId() {
        return userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
}