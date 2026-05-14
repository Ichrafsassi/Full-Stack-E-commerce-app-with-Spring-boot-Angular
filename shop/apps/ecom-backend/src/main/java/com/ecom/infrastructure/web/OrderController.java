package com.ecom.infrastructure.web;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.application.order.OrderService;
import com.ecom.domain.order.AppOrder;
import com.ecom.domain.order.OrderItem;
import com.ecom.infrastructure.security.JwtService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping
	public ResponseEntity<OrderResponse> placeOrder(
		Authentication authentication,
		@Valid @RequestBody CreateOrderRequest request
	) {
		JwtService.JwtUser jwtUser = (JwtService.JwtUser) authentication.getPrincipal();
		AppOrder order = orderService.placeOrder(
			jwtUser.userId(),
			request.customerName(),
			request.email(),
			request.address(),
			request.city(),
			request.country(),
			request.items().stream().map(line -> new OrderService.OrderLine(line.productId(), line.quantity())).toList()
		);
		return ResponseEntity.ok(OrderResponse.from(order));
	}

	@GetMapping("/my")
	public ResponseEntity<List<OrderResponse>> myOrders(Authentication authentication) {
		JwtService.JwtUser jwtUser = (JwtService.JwtUser) authentication.getPrincipal();
		return ResponseEntity.ok(orderService.listMyOrders(jwtUser.userId()).stream().map(OrderResponse::from).toList());
	}

	public record CreateOrderRequest(
		@NotBlank String customerName,
		@Email @NotBlank String email,
		@NotBlank String address,
		@NotBlank String city,
		@NotBlank String country,
		@NotEmpty List<OrderLineRequest> items
	) {
	}

	public record OrderLineRequest(@NotNull UUID productId, @Min(1) int quantity) {
	}

	public record OrderItemResponse(
		UUID productId,
		String productName,
		BigDecimal unitPrice,
		int quantity,
		BigDecimal lineTotal
	) {
		static OrderItemResponse from(OrderItem item) {
			return new OrderItemResponse(
				item.getProductId(),
				item.getProductName(),
				item.getUnitPrice(),
				item.getQuantity(),
				item.getLineTotal()
			);
		}
	}

	public record OrderResponse(
		UUID id,
		Instant createdAt,
		String status,
		BigDecimal total,
		List<OrderItemResponse> items,
		String customerName,
		String email,
		String address,
		String city,
		String country
	) {
		static OrderResponse from(AppOrder order) {
			return new OrderResponse(
				order.getId(),
				order.getCreatedAt(),
				order.getStatus().name(),
				order.getTotal(),
				order.getItems().stream().map(OrderItemResponse::from).toList(),
				order.getCustomerName(),
				order.getEmail(),
				order.getAddress(),
				order.getCity(),
				order.getCountry()
			);
		}
	}
}

