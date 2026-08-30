package com.orderflow.order.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Payment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String orderId;
	
	@Column(nullable = false)
	private String paymentId;
	
	@Column(nullable = false)
	private boolean paymentSuccessful;
	
	@Column(nullable = false)
	private BigDecimal totalCost;
	
	@Column(nullable = false)
	private String customerId;
	

}
