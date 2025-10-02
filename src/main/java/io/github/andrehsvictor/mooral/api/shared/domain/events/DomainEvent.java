package io.github.andrehsvictor.mooral.api.shared.domain.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Base class for all domain events in the application.
 * Provides common properties and behavior for event-driven architecture.
 */
public abstract class DomainEvent {
    
    private final UUID eventId;
    private final Instant occurredOn;
    private final String eventType;
    
    protected DomainEvent() {
        this.eventId = UUID.randomUUID();
        this.occurredOn = Instant.now();
        this.eventType = this.getClass().getSimpleName();
    }
    
    public UUID getEventId() {
        return eventId;
    }
    
    public Instant getOccurredOn() {
        return occurredOn;
    }
    
    public String getEventType() {
        return eventType;
    }
}