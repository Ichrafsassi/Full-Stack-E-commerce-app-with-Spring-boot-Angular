package com.ecom.infrastructure.web;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.application.order.OrderService;
import com.ecom.domain.order.AppOrder;
import com.ecom.domain.order.OrderItem;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {
	private final OrderService orderService;

	public AdminOrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@GetMapping
	public ResponseEntity<List<OrderSummary>> listOrders() {
		return ResponseEntity.ok(orderService.listAllOrders().stream().map(OrderSummary::from).toList());
	}

	public record OrderItemSummary(UUID productId, String productName, int quantity, BigDecimal lineTotal) {
		static OrderItemSummary from(OrderItem item) {
			return new OrderItemSummary(item.getProductId(), item.getProductName(), item.getQuantity(), item.getLineTotal());
		}
	}

	public record OrderSummary(
		UUID id,
		Instant createdAt,
		String status,
		BigDecimal total,
		String customerName,
		String email,
		List<OrderItemSummary> items
	) {
		static OrderSummary from(AppOrder order) {
			return new OrderSummary(
				order.getId(),
				order.getCreatedAt(),
				order.getStatus().name(),
				order.getTotal(),
				order.getCustomerName(),
				order.getEmail(),
				order.getItems().stream().map(OrderItemSummary::from).toList()
			);
		}
	}
}

