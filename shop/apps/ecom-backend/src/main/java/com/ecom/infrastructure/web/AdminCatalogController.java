package com.ecom.infrastructure.web;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.application.catalog.CatalogService;
import com.ecom.domain.catalog.Category;
import com.ecom.domain.catalog.Product;

@RestController
@RequestMapping("/api/admin/catalog")
public class AdminCatalogController {
	private final CatalogService catalogService;

	public AdminCatalogController(CatalogService catalogService) {
		this.catalogService = catalogService;
	}

	@GetMapping("/categories")
	public ResponseEntity<List<CategoryResponse>> categories() {
		return ResponseEntity.ok(catalogService.listCategories().stream().map(CategoryResponse::from).toList());
	}

	@PostMapping("/categories")
	public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody UpsertCategoryRequest request) {
		return ResponseEntity.ok(CategoryResponse.from(catalogService.createCategory(request.name())));
	}

	@DeleteMapping("/categories/{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
		catalogService.deleteCategory(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/products")
	public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody UpsertProductRequest request) {
		Product product = catalogService.createProduct(
			request.name(),
			request.description(),
			request.price(),
			request.stock(),
			request.categoryId(),
			request.imageUrl(),
			request.size()
		);
		return ResponseEntity.ok(ProductResponse.from(product));
	}

	@PutMapping("/products/{id}")
	public ResponseEntity<ProductResponse> updateProduct(
		@PathVariable UUID id,
		@Valid @RequestBody UpsertProductRequest request
	) {
		Product product = catalogService.updateProduct(
			id,
			request.name(),
			request.description(),
			request.price(),
			request.stock(),
			request.categoryId(),
			request.imageUrl(),
			request.size(),
			request.active() == null ? true : request.active()
		);
		return ResponseEntity.ok(ProductResponse.from(product));
	}

	@DeleteMapping("/products/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
		catalogService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}

	public record UpsertCategoryRequest(@NotBlank String name) {
	}

	public record CategoryResponse(UUID id, String name, String slug) {
		static CategoryResponse from(Category category) {
			return new CategoryResponse(category.getId(), category.getName(), category.getSlug());
		}
	}

	public record UpsertProductRequest(
		@NotBlank String name,
		@NotBlank String description,
		@NotNull @Positive BigDecimal price,
		@NotNull Integer stock,
		@NotNull UUID categoryId,
		String imageUrl,
		String size,
		Boolean active
	) {
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

