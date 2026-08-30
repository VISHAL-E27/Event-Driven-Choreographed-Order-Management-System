package com.orderflow.order.kafka;

import com.orderflow.common.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderKafkaProducer {

    public static final String ORDER_EVENTS_TOPIC = "order-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Publishing OrderCreatedEvent for orderId: {} to topic: {}",
                event.getOrderId(), ORDER_EVENTS_TOPIC);

        kafkaTemplate.send(ORDER_EVENTS_TOPIC, event.getOrderId(), event);
    }
}
