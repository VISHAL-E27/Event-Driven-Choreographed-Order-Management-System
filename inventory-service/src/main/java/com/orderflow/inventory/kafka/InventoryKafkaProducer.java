package com.orderflow.inventory.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.orderflow.common.event.InventoryReservedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InventoryKafkaProducer {

	private final KafkaTemplate<String,InventoryReservedEvent>kafkaTemplate;
	
	private final String INVENTORY_RESERVED_TOPIC = "inventory-events";
	
	public void sendInventoryReservedEvent(InventoryReservedEvent event) {
		kafkaTemplate.send(INVENTORY_RESERVED_TOPIC,event.getOrderId(),event);
		
	}
	
	
}
