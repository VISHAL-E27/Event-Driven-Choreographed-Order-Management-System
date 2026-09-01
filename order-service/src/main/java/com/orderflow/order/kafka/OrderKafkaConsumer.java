package com.orderflow.order.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.orderflow.common.event.InventoryReservedEvent;
import com.orderflow.common.event.PaymentCompletedEvent;
import com.orderflow.order.service.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderKafkaConsumer {
	
	private final OrderService orderService;
	
	private final String INVENTORY_TOPIC = "inventory-events";
	private final String PAYMENT_COMPLETED_TOPIC = "payment-completed";
	private final String GROUP_ID = "order-group";
	
	@KafkaListener(topics = INVENTORY_TOPIC, groupId = GROUP_ID)
	public void handleInventoryEvent(InventoryReservedEvent event) {
		log.info("Received InventoryReservedEvent for Order ID: {}, StockAvailable: {}", event.getOrderId(), event.isStockAvailable());
		orderService.updateOrderStatusFromInventory(event);
	}

	@KafkaListener(topics = PAYMENT_COMPLETED_TOPIC, groupId = GROUP_ID)
	public void handlePaymentCompletedEvent(PaymentCompletedEvent event) {
		log.info("Received PaymentCompletedEvent for Order ID: {}, PaymentSuccessful: {}", event.getOrderId(), event.isPaymentSuccessful());
		orderService.updateOrderStatusFromPayment(event);
	}
}
