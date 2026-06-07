package com.cortex.common.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {
    private Long id;
    private Long userId;
    private String status;
    private BigDecimal total;
    private List<OrderItemDTO> items;
    private LocalDateTime createdAt;

    public OrderDTO() {}

    public OrderDTO(Long id, Long userId, String status, BigDecimal total,
                    List<OrderItemDTO> items, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.total = total;
        this.items = items;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public List<OrderItemDTO> getItems() { return items; }
    public void setItems(List<OrderItemDTO> items) { this.items = items; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
