package com.orderflow.notification.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.orderflow.common.event.InventoryReservedEvent;
import com.orderflow.common.event.OrderCreatedEvent;
import com.orderflow.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class NotificationKafkaConsumer {
	
	private final NotificationService notificationService;
	
	private final String GROUP_ID = "notification-group";

	private final String ORDER_CREATED_TOPIC = "order-events";
			
	private final String INVENTORY_TOPIC = "inventory-events";
	
	@KafkaListener(groupId = GROUP_ID,topics=ORDER_CREATED_TOPIC)
	public void handleOrderCreatedEvent(OrderCreatedEvent event) {
		
		notificationService.sendOrderReceivedNotification(event);
	}
	
	@KafkaListener(groupId = GROUP_ID,topics=INVENTORY_TOPIC)
	public void handleInventoryEvent(InventoryReservedEvent event) {
		notificationService.sendInventoryStatusNotification(event);
	}
}
