package com.orderflow.common.event;

import java.util.List;
import java.util.UUID;

import com.orderflow.common.dto.OrderItemDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryReservedEvent {
	private UUID eventId;
	private String orderId;
	private String customerId;
	private boolean stockAvailable;
	private String failureReason;
	private List<OrderItemDto> items;
}
