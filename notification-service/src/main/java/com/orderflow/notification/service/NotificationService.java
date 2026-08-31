package com.orderflow.notification.service;

import org.springframework.stereotype.Service;

import com.orderflow.common.event.InventoryReservedEvent;
import com.orderflow.common.event.OrderCreatedEvent;
import com.orderflow.common.event.PaymentCompletedEvent;

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
			log.info("[NOTIFICATION - STOCK RESERVED] Order ID: {} | Stock Reserved Successfully!",
					event.getOrderId());
		} else {
			log.warn("[NOTIFICATION - STOCK FAILED] Order ID: {} | Reason: {}",
					event.getOrderId(), event.getFailureReason());
		}
	}

	public void sendPaymentNotification(PaymentCompletedEvent event) {
		if (event.isPaymentSuccessful()) {
			log.info("[NOTIFICATION - PAYMENT SUCCESS] Email sent to: {} | Order ID: {} | Payment ID: {}",
					event.getCustomerEmail(), event.getOrderId(), event.getPaymentId());
		} else {
			log.warn("[NOTIFICATION - PAYMENT FAILED] Order ID: {} | Reason: {}",
					event.getOrderId(), event.getFailureReason());
		}
	}
}
