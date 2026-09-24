package com.orderflow.order.dto;

import java.util.List;

import com.orderflow.common.dto.OrderItemDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description="Request payload for creating a new order")
public class CreateOrderRequest {

	@Schema(description="Unique Id of the customer placing the order",example="CUST-1001",requiredMode=Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Customer ID is required")
    private String customerId;

	@Schema(description = "Email address for order notifications", example = "abc@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid customer email format")
    private String customerEmail;

	@Schema(description = "List of order items to purchase")
    @NotEmpty(message = "Order must contain at least one item")
    private List<OrderItemDto> items;
}
