package com.orderflow.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Response containing product stock details")
public class InventoryResponse {

    @Schema(description = "Database ID of inventory record", example = "1")
    private Long id;

    @Schema(description = "Product identifier", example = "PROD-101")
    private String productId;

    @Schema(description = "Product name", example = "Wireless Mechanical Keyboard")
    private String productName;

    @Schema(description = "Quantity currently available for sale", example = "45")
    private Integer availableQuantity;

    @Schema(description = "Quantity reserved for processing orders", example = "5")
    private Integer reservedQuantity;
}
