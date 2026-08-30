package com.orderflow.inventory.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orderflow.common.dto.ApiResponse;
import com.orderflow.inventory.dto.AddStockRequest;
import com.orderflow.inventory.dto.InventoryResponse;
import com.orderflow.inventory.service.InventoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

	private final InventoryService inventoryService;

	@PostMapping("/add")
	public ResponseEntity<ApiResponse<InventoryResponse>> addStock(
			@Valid @RequestBody AddStockRequest request) {
		InventoryResponse response = inventoryService.addStock(request);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.success("Stock added successfully", response));
	}

	@GetMapping("/check")
	public ResponseEntity<ApiResponse<InventoryResponse>> getInventory(
			@RequestParam String productId) {
		InventoryResponse response = inventoryService.getInventoryByProductId(productId);
		return ResponseEntity.ok(ApiResponse.success("Inventory retrieved successfully", response));
	}

	@GetMapping("/all")
	public ResponseEntity<ApiResponse<List<InventoryResponse>>> getAllInventory() {
		List<InventoryResponse> response = inventoryService.getAllInventory();
		return ResponseEntity.ok(ApiResponse.success("All inventory items retrieved successfully", response));
	}

}
