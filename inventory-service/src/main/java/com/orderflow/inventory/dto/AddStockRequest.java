package com.orderflow.inventory.dto;

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
	
	@NotEmpty(message="Product Id is required")
	private String productId;
	
	@NotEmpty(message = "Product Name is required")
	private String productName;
	
	@NotNull(message = "Quantity is required")
	@Min(value=1, message = "Quantity must be at least 1")
	private Integer quantity; 

}
