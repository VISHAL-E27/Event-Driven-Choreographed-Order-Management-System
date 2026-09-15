package com.orderflow.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orderflow.common.dto.ApiResponse;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fallback")
public class FallbackController {
	
	@RequestMapping("/orderService")
	public Mono<ResponseEntity<ApiResponse<Void>>> orderServiceFallback(){
		return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(ApiResponse.error("Order Service is currently unavailable or taking too long to respond. Please try again later.")));
	}
	
	@RequestMapping("/inventoryService")
	public Mono<ResponseEntity<ApiResponse<Void>>> inventoryServiceFallback(){
		return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(ApiResponse.error("Inventory Service is currently unavailable or taking too long to respond. Please try again later.")));
	}
	
	@RequestMapping("/paymentService")
	public Mono<ResponseEntity<ApiResponse<Void>>> paymentServiceFallback(){
		return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(ApiResponse.error("Payment service is currently unavailable or taking too long to respond. Please try again later .")));
	}
	
	

}
