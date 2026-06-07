package com.cortex.common.event;

import java.math.BigDecimal;

public class OrderCreatedEvent extends BaseEvent {
    private Long orderId;
    private Long userId;
    private BigDecimal total;

    public OrderCreatedEvent() {}

    public OrderCreatedEvent(Long orderId, Long userId, BigDecimal total) {
        super();
        this.orderId = orderId;
        this.userId = userId;
        this.total = total;
    }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
}
