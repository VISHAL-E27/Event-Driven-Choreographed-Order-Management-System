package com.orderflow.inventory.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orderflow.common.dto.OrderItemDto;
import com.orderflow.common.event.InventoryReservedEvent;
import com.orderflow.common.event.OrderCreatedEvent;
import com.orderflow.common.event.PaymentCompletedEvent;
import com.orderflow.common.exception.ResourceNotFoundException;
import com.orderflow.inventory.dto.AddStockRequest;
import com.orderflow.inventory.dto.InventoryResponse;
import com.orderflow.inventory.entity.Inventory;
import com.orderflow.inventory.entity.ProcessedEvent;
import com.orderflow.inventory.kafka.InventoryKafkaProducer;
import com.orderflow.inventory.repository.InventoryRepository;
import com.orderflow.inventory.repository.ProcessedEventRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

	private final InventoryRepository inventoryRepository;
	private final InventoryKafkaProducer inventoryKafkaProducer;
	private final ProcessedEventRepository processedEventRepository;

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
		if (event.getEventId() != null && processedEventRepository.existsById(event.getEventId())) {
			log.info("OrderCreatedEvent with eventId {} already processed in Inventory. Skipping.", event.getEventId());
			return;
		}

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

		if (event.getEventId() != null) {
			processedEventRepository.save(ProcessedEvent.builder()
					.eventId(event.getEventId())
					.eventType("OrderCreatedEvent")
					.processedAt(LocalDateTime.now())
					.build());
		}

		InventoryReservedEvent reservedEvent = InventoryReservedEvent.builder()
				.eventId(UUID.randomUUID())
				.customerId(event.getCustomerId())
				.orderId(event.getOrderId())
				.stockAvailable(stockAvailable)
				.failureReason(failureReason)
				.items(event.getItems())
				.build();

		inventoryKafkaProducer.sendInventoryReservedEvent(reservedEvent);
	}

	@Transactional
	public void compensateStock(PaymentCompletedEvent event) {
		if (event.getItems() == null) {
			return;
		}

		if (event.getEventId() != null && processedEventRepository.existsById(event.getEventId())) {
			log.info("PaymentCompletedEvent (compensation) with eventId {} already processed in Inventory. Skipping.", event.getEventId());
			return;
		}
		
		if (event.isPaymentSuccessful()) {
			for (OrderItemDto item : event.getItems()) {
				Inventory inventory = inventoryRepository.findByProductId(item.getProductId());
				if (inventory != null) {
					inventory.setReservedQuantity(Math.max(0, inventory.getReservedQuantity() - item.getQuantity()));
					inventoryRepository.save(inventory);
				}
			}
			if (event.getEventId() != null) {
				processedEventRepository.save(ProcessedEvent.builder()
						.eventId(event.getEventId())
						.eventType("PaymentCompletedEvent_Success")
						.processedAt(LocalDateTime.now())
						.build());
			}
			return;
		}

		if (event.getFailureReason() != null && event.getFailureReason().contains("Insufficient stock")) {
			log.info("Order {} failed due to insufficient stock. No stock was reserved, skipping compensation.", event.getOrderId());
			if (event.getEventId() != null) {
				processedEventRepository.save(ProcessedEvent.builder()
						.eventId(event.getEventId())
						.eventType("PaymentCompletedEvent_SkippedStockCompensate")
						.processedAt(LocalDateTime.now())
						.build());
			}
			return;
		}

		log.info("Executing compensating transaction: Releasing reserved stock for order {}", event.getOrderId());
		for (OrderItemDto item : event.getItems()) {
			Inventory inventory = inventoryRepository.findByProductId(item.getProductId());
			if (inventory != null) {
				inventory.setAvailableQuantity(inventory.getAvailableQuantity() + item.getQuantity());
				inventory.setReservedQuantity(Math.max(0, inventory.getReservedQuantity() - item.getQuantity()));
				inventoryRepository.save(inventory);
				log.info("Unreserved {} units for ProductId: {}", item.getQuantity(), item.getProductId());
			}
		}

		if (event.getEventId() != null) {
			processedEventRepository.save(ProcessedEvent.builder()
					.eventId(event.getEventId())
					.eventType("PaymentCompletedEvent_Compensate")
					.processedAt(LocalDateTime.now())
					.build());
		}
	}

	@Transactional(readOnly = true)
	public InventoryResponse getInventoryByProductId(String productId) {
		Inventory inventory = inventoryRepository.findByProductId(productId);
		if (inventory == null) {
			throw new ResourceNotFoundException("Product not found in inventory: " + productId);
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


