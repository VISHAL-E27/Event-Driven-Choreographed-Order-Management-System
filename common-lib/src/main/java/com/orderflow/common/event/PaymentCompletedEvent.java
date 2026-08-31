package com.orderflow.common.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.orderflow.common.dto.OrderItemDto;
import com.orderflow.common.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentCompletedEvent {
	private UUID eventId;
	private String orderId;
	private String paymentId;
	private BigDecimal totalAmount;
	private boolean paymentSuccessful;
	private String customerId;
	private String customerEmail;
	private PaymentStatus status;
	private String failureReason;
	private List<OrderItemDto> items;
	private LocalDateTime eventDate;
}
