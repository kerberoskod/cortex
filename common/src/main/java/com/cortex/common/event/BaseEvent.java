package com.cortex.common.event;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class BaseEvent {
    private String eventId;
    private LocalDateTime timestamp;
    private String source;

    protected BaseEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
    }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
}
