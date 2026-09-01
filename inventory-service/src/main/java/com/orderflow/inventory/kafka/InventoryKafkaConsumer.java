package com.orderflow.inventory.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.orderflow.common.event.OrderCreatedEvent;
import com.orderflow.common.event.PaymentCompletedEvent;
import com.orderflow.inventory.service.InventoryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class InventoryKafkaConsumer {
	
	private final InventoryService inventoryService;
	
	private final String ORDER_CREATED_TOPIC = "order-events";
	private final String PAYMENT_COMPLETED_TOPIC = "payment-completed";
	
	private final String GROUP_ID = "inventory-group";
	
	@KafkaListener(topics = ORDER_CREATED_TOPIC, groupId = GROUP_ID)
	public void handleOrderCreatedEvent(OrderCreatedEvent event) {
		inventoryService.reserveStock(event);
	}

	@KafkaListener(topics = PAYMENT_COMPLETED_TOPIC, groupId = GROUP_ID)
	public void handlePaymentCompletedEvent(PaymentCompletedEvent event) {
		inventoryService.compensateStock(event);
	}

}

