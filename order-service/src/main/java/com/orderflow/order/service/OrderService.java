package com.orderflow.order.service;

import com.orderflow.common.dto.OrderItemDto;
import com.orderflow.common.enums.OrderStatus;
import com.orderflow.common.event.OrderCreatedEvent;
import com.orderflow.order.dto.CreateOrderRequest;
import com.orderflow.order.dto.OrderResponse;
import com.orderflow.order.entity.Order;
import com.orderflow.order.entity.OrderItem;
import com.orderflow.order.kafka.OrderKafkaProducer;
import com.orderflow.order.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

        private final OrderRepository orderRepository;
        private final OrderKafkaProducer orderKafkaProducer;

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