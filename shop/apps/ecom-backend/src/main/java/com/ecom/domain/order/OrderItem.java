package com.ecom.domain.order;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItem {
	@Id
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "order_id", nullable = false)
	private AppOrder order;

	@Column(nullable = false)
	private UUID productId;

	@Column(nullable = false)
	private String productName;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal unitPrice;

	@Column(nullable = false)
	private int quantity;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal lineTotal;

	protected OrderItem() {
	}

	public OrderItem(UUID productId, String productName, BigDecimal unitPrice, int quantity) {
		this.id = UUID.randomUUID();
		this.productId = productId;
		this.productName = productName;
		this.unitPrice = unitPrice;
		this.quantity = quantity;
		this.lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
	}

	@PrePersist
	void prePersist() {
		if (this.id == null) {
			this.id = UUID.randomUUID();
		}
		if (this.lineTotal == null) {
			this.lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
		}
	}

	public UUID getId() {
		return id;
	}

	public AppOrder getOrder() {
		return order;
	}

	public UUID getProductId() {
		return productId;
	}

	public String getProductName() {
		return productName;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public BigDecimal getLineTotal() {
		return lineTotal;
	}

	void setOrder(AppOrder order) {
		this.order = order;
	}
}
