package com.ecom.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.domain.catalog.Category;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
	Optional<Category> findBySlug(String slug);

	boolean existsByNameIgnoreCase(String name);
}

