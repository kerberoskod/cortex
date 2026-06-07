package com.cortex.notification.service;

import com.cortex.common.event.OrderCreatedEvent;
import com.cortex.common.event.UserRegisteredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @EventListener
    public void handleUserRegistered(UserRegisteredEvent event) {
        log.info("=== NOTIFICATION: Welcome email sent to {} (user #{}) ===", event.getEmail(), event.getUserId());
    }

    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("=== NOTIFICATION: Order confirmation #{} sent to user #{} (total: ${}) ===",
                event.getOrderId(), event.getUserId(), event.getTotal());
    }
}
