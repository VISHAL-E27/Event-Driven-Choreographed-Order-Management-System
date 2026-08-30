package com.orderflow.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orderflow.order.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment,Long>{
	

}
