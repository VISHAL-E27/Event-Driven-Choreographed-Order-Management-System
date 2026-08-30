package com.orderflow.inventory.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long>{
	
	public Inventory findByProductId(String id);
	

}
