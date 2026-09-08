package com.orderflow.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.orderflow.inventory.entity.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long>{
	
	public Inventory findByProductId(String id);
	

}
