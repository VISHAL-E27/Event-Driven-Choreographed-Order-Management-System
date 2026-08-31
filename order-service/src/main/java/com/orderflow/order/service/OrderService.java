package com.orderflow.order.service;

import com.orderflow.common.dto.OrderItemDto;
import com.orderflow.common.enums.OrderStatus;
import com.orderflow.common.event.OrderCreatedEvent;
import com.orderflow.common.event.InventoryReservedEvent;
import com.orderflow.common.event.PaymentCompletedEvent;
import com.orderflow.order.dto.CreateOrderRequest;
import com.orderflow.order.dto.OrderResponse;
import com.orderflow.order.entity.Order;
import com.orderflow.order.entity.OrderItem;
import com.orderflow.order.entity.ProcessedEvent;
import com.orderflow.order.kafka.OrderKafkaProducer;
import com.orderflow.order.repo.OrderRepository;
import com.orderflow.order.repo.ProcessedEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

        private final OrderRepository orderRepository;
        private final OrderKafkaProducer orderKafkaProducer;
        private final ProcessedEventRepository processedEventRepository;

        @Transactional
        public OrderResponse createOrder(CreateOrderRequest request) {
                log.info("Creating new order for customer: {}", request.getCustomerId());

                BigDecimal totalAmount = request.getItems().stream()
                                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                Order order = Order.builder()
                                .customerId(request.getCustomerId())
                                .customerEmail(request.getCustomerEmail())
                                .totalAmount(totalAmount)
                                .status(OrderStatus.PENDING)
                                .build();

                for (OrderItemDto itemDto : request.getItems()) {
                        OrderItem item = OrderItem.builder()
                                        .productId(itemDto.getProductId())
                                        .productName(itemDto.getProductName())
                                        .quantity(itemDto.getQuantity())
                                        .price(itemDto.getPrice())
                                        .build();
                        order.addItem(item);
                }

                Order savedOrder = orderRepository.save(order);
                log.info("Order saved successfully in DB with ID: {}", savedOrder.getId());

                OrderCreatedEvent event = OrderCreatedEvent.builder()
                                .eventId(UUID.randomUUID())
                                .orderId(savedOrder.getId())
                                .customerId(savedOrder.getCustomerId())
                                .customerEmail(savedOrder.getCustomerEmail())
                                .totalAmount(savedOrder.getTotalAmount())
                                .status(savedOrder.getStatus())
                                .items(request.getItems())
                                .createdAt(savedOrder.getCreatedAt())
                                .build();

                orderKafkaProducer.sendOrderCreatedEvent(event);

                return mapToOrderResponse(savedOrder);
        }

        @Transactional(readOnly = true)
        public OrderResponse getOrderById(String orderId) {
                Order order = orderRepository.findById(orderId)
                                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
                return mapToOrderResponse(order);
        }

        @Transactional
        public void updateOrderStatusFromInventory(InventoryReservedEvent event) {
                if (event.getEventId() != null && processedEventRepository.existsById(event.getEventId())) {
                        log.info("InventoryReservedEvent with eventId {} already processed. Skipping.", event.getEventId());
                        return;
                }

                if (!event.isStockAvailable()) {
                        log.info("Stock unavailable for Order ID: {}. Updating status to FAILED.", event.getOrderId());
                        orderRepository.findById(event.getOrderId()).ifPresent(order -> {
                                order.setStatus(OrderStatus.FAILED);
                                orderRepository.save(order);
                        });
                }

                if (event.getEventId() != null) {
                        processedEventRepository.save(ProcessedEvent.builder()
                                        .eventId(event.getEventId())
                                        .eventType("InventoryReservedEvent")
                                        .processedAt(LocalDateTime.now())
                                        .build());
                }
        }

        @Transactional
        public void updateOrderStatusFromPayment(PaymentCompletedEvent event) {
                if (event.getEventId() != null && processedEventRepository.existsById(event.getEventId())) {
                        log.info("PaymentCompletedEvent with eventId {} already processed. Skipping.", event.getEventId());
                        return;
                }

                log.info("Updating order status for Order ID: {} based on payment status: {}", event.getOrderId(), event.isPaymentSuccessful());
                orderRepository.findById(event.getOrderId()).ifPresent(order -> {
                        if (event.isPaymentSuccessful()) {
                                order.setStatus(OrderStatus.CONFIRMED);
                        } else {
                                order.setStatus(OrderStatus.FAILED);
                        }
                        orderRepository.save(order);
                });

                if (event.getEventId() != null) {
                        processedEventRepository.save(ProcessedEvent.builder()
                                        .eventId(event.getEventId())
                                        .eventType("PaymentCompletedEvent")
                                        .processedAt(LocalDateTime.now())
                                        .build());
                }
        }

        private OrderResponse mapToOrderResponse(Order order) {
                List<OrderItemDto> itemDtos = order.getItems().stream()
                                .map(item -> OrderItemDto.builder()
                                                .productId(item.getProductId())
                                                .productName(item.getProductName())
                                                .quantity(item.getQuantity())
                                                .price(item.getPrice())
                                                .build())
                                .collect(Collectors.toList());

                return OrderResponse.builder()
                                .orderId(order.getId())
                                .customerId(order.getCustomerId())
                                .customerEmail(order.getCustomerEmail())
                                .totalAmount(order.getTotalAmount())
                                .status(order.getStatus())
                                .items(itemDtos)
                                .createdAt(order.getCreatedAt())
                                .build();
        }
}