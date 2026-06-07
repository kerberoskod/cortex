package com.cortex.order;

import com.cortex.common.dto.CreateOrderRequest;
import com.cortex.common.dto.OrderItemDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() {}

    @Test
    void shouldCreateOrder() throws Exception {
        var items = List.of(
                new OrderItemDTO(1L, "Product A", 2, new BigDecimal("19.99")),
                new OrderItemDTO(2L, "Product B", 1, new BigDecimal("49.99"))
        );
        var request = new CreateOrderRequest(items);

        mockMvc.perform(post("/api/orders")
                .header("X-User-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.items.length()").value(2));
    }

    @Test
    void shouldGetUserOrders() throws Exception {
        var items = List.of(
                new OrderItemDTO(1L, "Product A", 1, new BigDecimal("9.99"))
        );
        var request = new CreateOrderRequest(items);

        mockMvc.perform(post("/api/orders")
                .header("X-User-Id", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/orders")
                .header("X-User-Id", "42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldGetOrderById() throws Exception {
        var items = List.of(
                new OrderItemDTO(1L, "Test Item", 1, new BigDecimal("5.00"))
        );
        var request = new CreateOrderRequest(items);

        var result = mockMvc.perform(post("/api/orders")
                .header("X-User-Id", "99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        var responseJson = result.getResponse().getContentAsString();
        var created = objectMapper.readTree(responseJson);
        var orderId = created.get("id").asLong();

        mockMvc.perform(get("/api/orders/" + orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId));
    }

    @Test
    void shouldReturn404ForUnknownOrder() throws Exception {
        mockMvc.perform(get("/api/orders/99999"))
                .andExpect(status().isNotFound());
    }
}
