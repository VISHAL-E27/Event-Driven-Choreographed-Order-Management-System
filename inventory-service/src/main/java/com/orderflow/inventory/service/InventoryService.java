package com.orderflow.inventory.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orderflow.common.dto.OrderItemDto;
import com.orderflow.common.event.InventoryReservedEvent;
import com.orderflow.common.event.OrderCreatedEvent;
import com.orderflow.inventory.dto.AddStockRequest;
import com.orderflow.inventory.dto.InventoryResponse;
import com.orderflow.inventory.entity.Inventory;
import com.orderflow.inventory.entity.InventoryRepository;
import com.orderflow.inventory.kafka.InventoryKafkaProducer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {

	private final InventoryRepository inventoryRepository;
	private final InventoryKafkaProducer inventoryKafkaProducer;

	@Transactional
	public InventoryResponse addStock(AddStockRequest request) {
		Inventory inventory = inventoryRepository.findByProductId(request.getProductId());
		if (inventory == null) {
			inventory = Inventory.builder()
					.productId(request.getProductId())
					.productName(request.getProductName())
					.availableQuantity(request.getQuantity())
					.reservedQuantity(0)
					.build();
		} else {
			inventory.setAvailableQuantity(inventory.getAvailableQuantity() + request.getQuantity());
		}
		Inventory saved = inventoryRepository.save(inventory);
		return mapToResponse(saved);
	}

	@Transactional
	public void reserveStock(OrderCreatedEvent event) {
		boolean stockAvailable = true;
		String failureReason = null;

		for (OrderItemDto item : event.getItems()) {
			Inventory inventory = inventoryRepository.findByProductId(item.getProductId());
			if (inventory == null || inventory.getAvailableQuantity() < item.getQuantity()) {
				stockAvailable = false;
				failureReason = "Insufficient stock for ProductId: " + item.getProductId();
				break;
			}
		}
		if (stockAvailable) {
			for (OrderItemDto item : event.getItems()) {
				Inventory inventory = inventoryRepository.findByProductId(item.getProductId());
				inventory.setAvailableQuantity(inventory.getAvailableQuantity() - item.getQuantity());
				inventory.setReservedQuantity(inventory.getReservedQuantity() + item.getQuantity());
				inventoryRepository.save(inventory);
			}
		}

		InventoryReservedEvent reservedEvent = InventoryReservedEvent.builder()
				.eventId(UUID.randomUUID())
				.customerId(event.getCustomerId())
				.orderId(event.getOrderId())
				.stockAvailable(stockAvailable)
				.failureReason(failureReason)
				.build();

		inventoryKafkaProducer.sendInventoryReservedEvent(reservedEvent);
	}

	@Transactional(readOnly = true)
	public InventoryResponse getInventoryByProductId(String productId) {
		Inventory inventory = inventoryRepository.findByProductId(productId);
		if (inventory == null) {
			throw new RuntimeException("Product not found in inventory: " + productId);
		}
		return mapToResponse(inventory);
	}

	@Transactional(readOnly = true)
	public List<InventoryResponse> getAllInventory() {
		return inventoryRepository.findAll().stream()
				.map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	public InventoryResponse mapToResponse(Inventory inventory) {
		return InventoryResponse.builder()
				.id(inventory.getId())
				.productId(inventory.getProductId())
				.productName(inventory.getProductName())
				.availableQuantity(inventory.getAvailableQuantity())
				.reservedQuantity(inventory.getReservedQuantity())
				.build();
	}

}

