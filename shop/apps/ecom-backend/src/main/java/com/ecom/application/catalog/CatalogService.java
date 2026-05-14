package com.ecom.application.catalog;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.domain.catalog.Category;
import com.ecom.domain.catalog.Product;
import com.ecom.infrastructure.persistence.CategoryRepository;
import com.ecom.infrastructure.persistence.ProductRepository;

@Service
public class CatalogService {
	private final CategoryRepository categoryRepository;
	private final ProductRepository productRepository;

	public CatalogService(CategoryRepository categoryRepository, ProductRepository productRepository) {
		this.categoryRepository = categoryRepository;
		this.productRepository = productRepository;
	}

	@Transactional(readOnly = true)
	public List<Category> listCategories() {
		return categoryRepository.findAll().stream().sorted(Comparator.comparing(Category::getName)).toList();
	}

	@Transactional(readOnly = true)
	public List<Product> listProducts(UUID categoryId, String sort) {
		List<Product> products = categoryId == null
			? productRepository.findAllByActiveTrueOrderByCreatedAtDesc()
			: productRepository.findAllByActiveTrueAndCategory_IdOrderByCreatedAtDesc(categoryId);

		if (sort == null || sort.isBlank()) {
			return products;
		}

		return switch (sort) {
			case "price-asc" -> products.stream().sorted(Comparator.comparing(Product::getPrice)).toList();
			case "price-desc" -> products.stream().sorted(Comparator.comparing(Product::getPrice).reversed()).toList();
			case "newest" -> products.stream().sorted(Comparator.comparing(Product::getCreatedAt).reversed()).toList();
			case "oldest" -> products.stream().sorted(Comparator.comparing(Product::getCreatedAt)).toList();
			default -> products;
		};
	}

	@Transactional(readOnly = true)
	public Product getProduct(UUID productId) {
		return productRepository.findById(productId).orElseThrow(() -> new IllegalStateException("Product not found"));
	}

	@Transactional
	public Category createCategory(String name) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Category name is required");
		}
		if (categoryRepository.existsByNameIgnoreCase(name.trim())) {
			throw new IllegalArgumentException("Category already exists");
		}
		return categoryRepository.save(new Category(name.trim()));
	}

	@Transactional
	public void deleteCategory(UUID categoryId) {
		categoryRepository.deleteById(categoryId);
	}

	@Transactional
	public Product createProduct(
		String name,
		String description,
		BigDecimal price,
		int stock,
		UUID categoryId,
		String imageUrl,
		String size
	) {
		Category category = categoryRepository
			.findById(categoryId)
			.orElseThrow(() -> new IllegalArgumentException("Invalid category"));

		Product product = new Product(
			normalized(name),
			normalized(description),
			normalizedPrice(price),
			stock,
			category,
			blankToNull(imageUrl),
			blankToNull(size)
		);
		return productRepository.save(product);
	}

	@Transactional
	public Product updateProduct(
		UUID productId,
		String name,
		String description,
		BigDecimal price,
		int stock,
		UUID categoryId,
		String imageUrl,
		String size,
		boolean active
	) {
		Product product = productRepository
			.findById(productId)
			.orElseThrow(() -> new IllegalStateException("Product not found"));

		Category category = categoryRepository
			.findById(categoryId)
			.orElseThrow(() -> new IllegalArgumentException("Invalid category"));

		product.update(
			normalized(name),
			normalized(description),
			normalizedPrice(price),
			stock,
			category,
			blankToNull(imageUrl),
			blankToNull(size),
			active
		);

		return product;
	}

	@Transactional
	public void deleteProduct(UUID productId) {
		productRepository.deleteById(productId);
	}

	private static String normalized(String value) {
		if (value == null) throw new IllegalArgumentException("Required field missing");
		String trimmed = value.trim();
		if (trimmed.isBlank()) throw new IllegalArgumentException("Required field missing");
		return trimmed;
	}

	private static BigDecimal normalizedPrice(BigDecimal price) {
		if (price == null) throw new IllegalArgumentException("Price is required");
		if (price.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Price must be positive");
		return price;
	}

	private static String blankToNull(String value) {
		if (value == null) return null;
		String trimmed = value.trim();
		return trimmed.isBlank() ? null : trimmed;
	}
}

