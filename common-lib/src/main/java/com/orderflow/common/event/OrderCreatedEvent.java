package com.orderflow.common.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.orderflow.common.dto.OrderItemDto;
import com.orderflow.common.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreatedEvent {
	
	private UUID eventId;
	private String orderId;
	private String customerId;
	private String customerEmail;
	private OrderStatus status;
	private BigDecimal totalAmount;
	private List<OrderItemDto>items;
	private LocalDateTime createdAt;
}
