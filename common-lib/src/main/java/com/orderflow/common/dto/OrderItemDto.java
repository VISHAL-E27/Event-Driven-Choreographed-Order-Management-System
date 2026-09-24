package com.orderflow.common.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Individual item within an order")
public class OrderItemDto {

    @Schema(description = "Product identifier", example = "PROD-101")
    private String productId;

    @Schema(description = "Product name", example = "Wireless Mechanical Keyboard")
    private String productName;

    @Schema(description = "Quantity purchased", example = "2")
    private Integer quantity;

    @Schema(description = "Price per unit", example = "149.99")
    private BigDecimal price;
}
