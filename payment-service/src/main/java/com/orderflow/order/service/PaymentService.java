package com.orderflow.order.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orderflow.common.enums.PaymentStatus;
import com.orderflow.common.event.InventoryReservedEvent;
import com.orderflow.common.event.PaymentCompletedEvent;
import com.orderflow.order.entity.Payment;
import com.orderflow.order.entity.ProcessedEvent;
import com.orderflow.order.kafka.PaymentKafkaProducer;
import com.orderflow.order.repository.PaymentRepository;
import com.orderflow.order.repository.ProcessedEventRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentRepository paymentRepository;
	private final PaymentKafkaProducer paymentKafkaProducer;
	private final ProcessedEventRepository processedEventRepository;

	private final String PAYMENT_TOPIC = "payment-topic";

	@Transactional
	public void handlePayment(InventoryReservedEvent event) {
		if (event.getEventId() != null && processedEventRepository.existsById(event.getEventId())) {
			log.info("InventoryReservedEvent with eventId {} already processed in Payment. Skipping.", event.getEventId());
			return;
		}

		if(!event.isStockAvailable()) {
			log.info("Stock not available for order {}. Skipping payment.",event.getOrderId());
			PaymentCompletedEvent failedEvent = PaymentCompletedEvent.builder()
			.eventId(UUID.randomUUID())
			.orderId(event.getOrderId())
			.customerId(event.getCustomerId())
			.paymentId(null)
			.paymentSuccessful(false)
			.status(PaymentStatus.FAILED)
			.eventDate(LocalDateTime.now())
			.failureReason(event.getFailureReason())
			.items(event.getItems())
			.build();

			if (event.getEventId() != null) {
				processedEventRepository.save(ProcessedEvent.builder()
						.eventId(event.getEventId())
						.eventType("InventoryReservedEvent_FailedStock")
						.processedAt(LocalDateTime.now())
						.build());
			}

			paymentKafkaProducer.handlePaymentCompletedEvent(failedEvent);
            return;
		}
		else {
			log.info("Stock is available");
			String paymentId = UUID.randomUUID().toString();
			Payment payment = Payment.builder()
			.orderId(event.getOrderId())
			.customerId(event.getCustomerId())
			.paymentId(paymentId)
			.paymentSuccessful(true)
			.build();
			
			paymentRepository.save(payment);
			
			PaymentCompletedEvent paymentCompletedEvent = PaymentCompletedEvent.builder()
					.eventId(UUID.randomUUID())
					.orderId(event.getOrderId())
					.paymentId(paymentId)
					.customerId(event.getCustomerId())
					.paymentSuccessful(true)
					.status(PaymentStatus.SUCCESS)
					.eventDate(LocalDateTime.now())
					.items(event.getItems())
					.build();

			if (event.getEventId() != null) {
				processedEventRepository.save(ProcessedEvent.builder()
						.eventId(event.getEventId())
						.eventType("InventoryReservedEvent_Success")
						.processedAt(LocalDateTime.now())
						.build());
			}
			
			paymentKafkaProducer.handlePaymentCompletedEvent(paymentCompletedEvent);
		}
	}
}

