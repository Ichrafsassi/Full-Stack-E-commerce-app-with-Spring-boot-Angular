package com.ecom.infrastructure.bootstrap;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.domain.catalog.Category;
import com.ecom.domain.catalog.Product;
import com.ecom.infrastructure.persistence.CategoryRepository;
import com.ecom.infrastructure.persistence.ProductRepository;

@Component
@Profile("dev")
public class CatalogSeeder implements ApplicationRunner {
	private final CategoryRepository categoryRepository;
	private final ProductRepository productRepository;

	public CatalogSeeder(CategoryRepository categoryRepository, ProductRepository productRepository) {
		this.categoryRepository = categoryRepository;
		this.productRepository = productRepository;
	}

	@Override
	@Transactional
	public void run(ApplicationArguments args) {
		if (productRepository.count() > 0) {
			return;
		}

		Category audio = categoryRepository.save(new Category("Audio"));
		Category accessories = categoryRepository.save(new Category("Accessories"));
		Category desk = categoryRepository.save(new Category("Desk Setup"));
		Category wearables = categoryRepository.save(new Category("Wearables"));

		List<Product> products = List.of(
			new Product(
				"AeroFit Pro Headphones",
				"Premium wireless headphones with adaptive noise cancellation and long battery life.",
				new BigDecimal("199.00"),
				18,
				audio,
				"https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=900&q=80",
				null
			),
			new Product(
				"Halo Smart Watch",
				"Track workouts, sleep, and notifications with a sleek all-day smartwatch.",
				new BigDecimal("149.00"),
				26,
				wearables,
				"https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=900&q=80",
				"One size"
			),
			new Product(
				"Nimbus Mechanical Keyboard",
				"Tactile hot-swappable keyboard built for focused work and gaming sessions.",
				new BigDecimal("129.00"),
				14,
				desk,
				"https://images.unsplash.com/photo-1511467687858-23d96c32e4ae?auto=format&fit=crop&w=900&q=80",
				null
			),
			new Product(
				"Orbit Laptop Stand",
				"Ergonomic aluminum stand that improves posture and desk organization.",
				new BigDecimal("69.00"),
				42,
				accessories,
				"https://images.unsplash.com/photo-1516321318423-f06f85e504b3?auto=format&fit=crop&w=900&q=80",
				null
			),
			new Product(
				"Pulse Desk Lamp",
				"Minimal LED lamp with ambient modes for deep work, gaming, or evening wind-down.",
				new BigDecimal("89.00"),
				11,
				desk,
				"https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?auto=format&fit=crop&w=900&q=80",
				null
			),
			new Product(
				"Arc Wireless Charger",
				"Fast wireless charging pad with a compact footprint and non-slip finish.",
				new BigDecimal("39.00"),
				35,
				accessories,
				"https://images.unsplash.com/photo-1583394838336-acd977736f90?auto=format&fit=crop&w=900&q=80",
				null
			)
		);

		productRepository.saveAll(products);
	}
}

