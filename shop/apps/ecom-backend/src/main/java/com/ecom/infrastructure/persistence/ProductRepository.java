package com.ecom.infrastructure.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.domain.catalog.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {
	@EntityGraph(attributePaths = "category")
	List<Product> findAllByActiveTrueOrderByCreatedAtDesc();

	@EntityGraph(attributePaths = "category")
	List<Product> findAllByActiveTrueAndCategory_IdOrderByCreatedAtDesc(UUID categoryId);

	@EntityGraph(attributePaths = "category")
	java.util.Optional<Product> findById(UUID id);
}

