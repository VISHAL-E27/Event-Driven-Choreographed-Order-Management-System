package com.orderflow.notification.service;

import org.springframework.stereotype.Service;

import com.orderflow.common.event.InventoryReservedEvent;
import com.orderflow.common.event.OrderCreatedEvent;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@NoArgsConstructor
@Slf4j
public class NotificationService {

	public void sendOrderReceivedNotification(OrderCreatedEvent event) {
		log.info("[NOTIFICATION - ORDER RECEIVED] Email sent to: {} | Order ID: {} | Total: ${}",
				event.getCustomerEmail(), event.getOrderId(), event.getTotalAmount());
	}

	public void sendInventoryStatusNotification(InventoryReservedEvent event) {
		if (event.isStockAvailable()) {
            log.info("[NOTIFICATION - ORDER CONFIRMED] Order ID: {} | Stock Reserved Successfully!",
                    event.getOrderId());
        } else {
            log.warn("[NOTIFICATION - ORDER CANCELLED] Order ID: {} | Reason: {}",
                    event.getOrderId(), event.getFailureReason());
	}
}
}
