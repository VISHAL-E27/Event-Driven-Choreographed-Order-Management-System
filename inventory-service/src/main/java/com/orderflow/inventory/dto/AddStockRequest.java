package com.orderflow.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddStockRequest {
	
	@Schema(description="Unique ID of the product",example="1",requiredMode=Schema.RequiredMode.REQUIRED)
	@NotEmpty(message="Product Id is required")
	private String productId;
	
	@Schema(description="Unique ID of the product",example="Laptop",requiredMode=Schema.RequiredMode.REQUIRED)
	@NotEmpty(message = "Product Name is required")
	private String productName;
	
	@Schema(description="Quantity of the product",example="1",requiredMode=Schema.RequiredMode.REQUIRED)
	@NotNull(message = "Quantity is required")
	@Min(value=1, message = "Quantity must be at least 1")
	private Integer quantity; 

}
