package com.cortex.common.event;

public class UserRegisteredEvent extends BaseEvent {
    private Long userId;
    private String email;

    public UserRegisteredEvent() {}

    public UserRegisteredEvent(Long userId, String email) {
        super();
        this.userId = userId;
        this.email = email;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
