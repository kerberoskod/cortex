package com.cortex.common.event;

public class OrderShippedEvent extends BaseEvent {
    private Long orderId;
    private String trackingNumber;

    public OrderShippedEvent() {}

    public OrderShippedEvent(Long orderId, String trackingNumber) {
        super();
        this.orderId = orderId;
        this.trackingNumber = trackingNumber;
    }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
}
