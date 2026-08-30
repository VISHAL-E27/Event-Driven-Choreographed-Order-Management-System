package com.orderflow.order.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orderflow.common.dto.ApiResponse;
import com.orderflow.order.dto.CreateOrderRequest;
import com.orderflow.order.dto.OrderResponse;
import com.orderflow.order.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orderService")
@RequiredArgsConstructor
public class OrderServiceController {
	
	private final OrderService orderService;
	
	@PostMapping("/createOrder")
	public ResponseEntity<ApiResponse<OrderResponse>> createOrder( 
			@Valid @RequestBody CreateOrderRequest createOrderRequest){
		
		OrderResponse response = orderService.createOrder(createOrderRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Order Placed successfully,"
				+ "procesing in progress",response));
	}
	
	@GetMapping("/getOrder")
	public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@RequestParam String orderId) {
		
		
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Order retrieved successfully", orderService.getOrderById(orderId)));
	}
	
}
