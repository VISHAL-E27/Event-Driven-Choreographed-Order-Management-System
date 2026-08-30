package com.orderflow.order.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.orderflow.common.event.PaymentCompletedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class PaymentKafkaProducer {
	
	private final KafkaTemplate<String,PaymentCompletedEvent>kafkaTemplate;
	private final String PAYMENT_COMPLETED_TOPIC = "payment-completed";
	
	public void handlePaymentCompletedEvent(PaymentCompletedEvent event) {
		kafkaTemplate.send(PAYMENT_COMPLETED_TOPIC,event.getCustomerId(),event);
	}
}
