package com.ecom.application.order;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.domain.catalog.Product;
import com.ecom.domain.order.AppOrder;
import com.ecom.domain.order.OrderItem;
import com.ecom.domain.user.AppUser;
import com.ecom.infrastructure.persistence.AppUserRepository;
import com.ecom.infrastructure.persistence.OrderRepository;
import com.ecom.infrastructure.persistence.ProductRepository;

@Service
public class OrderService {
	private final OrderRepository orderRepository;
	private final AppUserRepository userRepository;
	private final ProductRepository productRepository;

	public OrderService(
		OrderRepository orderRepository,
		AppUserRepository userRepository,
		ProductRepository productRepository
	) {
		this.orderRepository = orderRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
	}

	@Transactional
	public AppOrder placeOrder(
		UUID userId,
		String customerName,
		String email,
		String address,
		String city,
		String country,
		List<OrderLine> items
	) {
		if (items == null || items.isEmpty()) {
			throw new IllegalArgumentException("Order must contain at least one item");
		}

		AppUser user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User not found"));
		AppOrder order = new AppOrder(user, required(customerName), required(email), required(address), required(city), required(country));

		for (OrderLine line : items) {
			if (line.quantity() <= 0) {
				throw new IllegalArgumentException("Quantity must be at least 1");
			}

			Product product = productRepository
				.findById(line.productId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid product"));

			if (!product.isActive()) {
				throw new IllegalArgumentException("Product not available");
			}

			if (product.getStock() < line.quantity()) {
				throw new IllegalArgumentException("Not enough stock for " + product.getName());
			}

			product.decrementStock(line.quantity());
			order.addItem(new OrderItem(product.getId(), product.getName(), scaled(product.getPrice()), line.quantity()));
		}

		order.recalculateTotal();
		return orderRepository.save(order);
	}

	@Transactional(readOnly = true)
	public List<AppOrder> listMyOrders(UUID userId) {
		return orderRepository.findAllByUser_IdOrderByCreatedAtDesc(userId);
	}

	@Transactional(readOnly = true)
	public List<AppOrder> listAllOrders() {
		return orderRepository.findAll().stream().sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt())).toList();
	}

	private static BigDecimal scaled(BigDecimal value) {
		return value.setScale(2);
	}

	private static String required(String value) {
		if (value == null) throw new IllegalArgumentException("Required field missing");
		String trimmed = value.trim();
		if (trimmed.isBlank()) throw new IllegalArgumentException("Required field missing");
		return trimmed;
	}

	public record OrderLine(UUID productId, int quantity) {
	}
}

