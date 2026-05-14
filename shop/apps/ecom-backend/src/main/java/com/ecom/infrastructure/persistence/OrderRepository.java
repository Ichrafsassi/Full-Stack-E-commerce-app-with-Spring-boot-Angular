package com.ecom.infrastructure.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.domain.order.AppOrder;

public interface OrderRepository extends JpaRepository<AppOrder, UUID> {
	List<AppOrder> findAllByUser_IdOrderByCreatedAtDesc(UUID userId);
}

