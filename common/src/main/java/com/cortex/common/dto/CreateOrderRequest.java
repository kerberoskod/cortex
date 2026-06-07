package com.cortex.common.dto;

import java.util.List;

public class CreateOrderRequest {
    private List<OrderItemDTO> items;

    public CreateOrderRequest() {}

    public CreateOrderRequest(List<OrderItemDTO> items) {
        this.items = items;
    }

    public List<OrderItemDTO> getItems() { return items; }
    public void setItems(List<OrderItemDTO> items) { this.items = items; }
}
