package com.orderflow.order.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.orderflow.common.event.InventoryReservedEvent;
import com.orderflow.order.service.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentKafkaConsumer {

	private final String INVENTORY_RESERVED_TOPIC="inventory-events";
	
	private final String PAYMENT_CONSUMER_GROUP = "payment-consumer";
	
	private final PaymentService service;
	
	@KafkaListener(topics=INVENTORY_RESERVED_TOPIC,groupId=PAYMENT_CONSUMER_GROUP)
	public void handleInventoryReservedEvent(InventoryReservedEvent event) {
		log.info(INVENTORY_RESERVED_TOPIC);
		service.handlePayment(event);
	}
}
