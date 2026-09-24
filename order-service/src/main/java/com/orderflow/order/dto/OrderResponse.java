package com.orderflow.order.dto;

import com.orderflow.common.dto.OrderItemDto;
import com.orderflow.common.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response details of an order")
public class OrderResponse {

    @Schema(description = "Order ID", example = "1")
    private String orderId;

    @Schema(description = "Customer ID", example = "CUST-1001")
    private String customerId;

    @Schema(description = "Customer email address", example = "abc@example.com")
    private String customerEmail;

    @Schema(description = "Total order cost in USD", example = "299.99")
    private BigDecimal totalAmount;

    @Schema(description = "Current Saga status of order", example = "PENDING")
    private OrderStatus status;

    @Schema(description = "Items included in this order")
    private List<OrderItemDto> items;

    @Schema(description = "Timestamp when order was placed")
    private LocalDateTime createdAt;
}
