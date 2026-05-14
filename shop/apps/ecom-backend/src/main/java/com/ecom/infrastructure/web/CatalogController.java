package com.ecom.infrastructure.web;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.application.catalog.CatalogService;
import com.ecom.domain.catalog.Category;
import com.ecom.domain.catalog.Product;

@RestController
@RequestMapping("/api")
public class CatalogController {
	private final CatalogService catalogService;

	public CatalogController(CatalogService catalogService) {
		this.catalogService = catalogService;
	}

	@GetMapping("/categories")
	public ResponseEntity<List<CategoryResponse>> categories() {
		return ResponseEntity.ok(catalogService.listCategories().stream().map(CategoryResponse::from).toList());
	}

	@GetMapping("/products")
	public ResponseEntity<List<ProductResponse>> products(
		@RequestParam(name = "categoryId", required = false) UUID categoryId,
		@RequestParam(name = "sort", required = false) String sort
	) {
		return ResponseEntity
			.ok(catalogService.listProducts(categoryId, sort).stream().map(ProductResponse::from).toList());
	}

	@GetMapping("/products/{id}")
	public ResponseEntity<ProductResponse> product(@PathVariable UUID id) {
		return ResponseEntity.ok(ProductResponse.from(catalogService.getProduct(id)));
	}

	public record CategoryResponse(UUID id, String name, String slug) {
		static CategoryResponse from(Category category) {
			return new CategoryResponse(category.getId(), category.getName(), category.getSlug());
		}
	}

	public record ProductResponse(
		UUID id,
		String name,
		String description,
		BigDecimal price,
		int stock,
		boolean active,
		String imageUrl,
		String size,
		UUID categoryId,
		String categoryName,
		Instant createdAt
	) {
		static ProductResponse from(Product product) {
			return new ProductResponse(
				product.getId(),
				product.getName(),
				product.getDescription(),
				product.getPrice(),
				product.getStock(),
				product.isActive(),
				product.getImageUrl(),
				product.getSize(),
				product.getCategory().getId(),
				product.getCategory().getName(),
				product.getCreatedAt()
			);
		}
	}
}

