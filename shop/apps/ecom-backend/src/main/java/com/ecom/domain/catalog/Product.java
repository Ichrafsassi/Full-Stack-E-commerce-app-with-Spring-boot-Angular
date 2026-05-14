package com.ecom.domain.catalog;

import java.math.BigDecimal;
import java.time.Instant;
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
@Table(name = "products")
public class Product {
	@Id
	private UUID id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, length = 2000)
	private String description;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal price;

	@Column(nullable = false)
	private int stock;

	@Column(nullable = false)
	private boolean active;

	@Column(nullable = true)
	private String imageUrl;

	@Column(nullable = true)
	private String size;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@Column(nullable = false)
	private Instant createdAt;

	protected Product() {
	}

	public Product(
		String name,
		String description,
		BigDecimal price,
		int stock,
		Category category,
		String imageUrl,
		String size
	) {
		this.id = UUID.randomUUID();
		this.name = name;
		this.description = description;
		this.price = price;
		this.stock = stock;
		this.category = category;
		this.imageUrl = imageUrl;
		this.size = size;
		this.active = true;
		this.createdAt = Instant.now();
	}

	@PrePersist
	void prePersist() {
		if (this.id == null) {
			this.id = UUID.randomUUID();
		}
		if (this.createdAt == null) {
			this.createdAt = Instant.now();
		}
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public int getStock() {
		return stock;
	}

	public boolean isActive() {
		return active;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getSize() {
		return size;
	}

	public Category getCategory() {
		return category;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void update(
		String name,
		String description,
		BigDecimal price,
		int stock,
		Category category,
		String imageUrl,
		String size,
		boolean active
	) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.stock = stock;
		this.category = category;
		this.imageUrl = imageUrl;
		this.size = size;
		this.active = active;
	}

	public void decrementStock(int quantity) {
		this.stock = Math.max(0, this.stock - quantity);
	}
}
