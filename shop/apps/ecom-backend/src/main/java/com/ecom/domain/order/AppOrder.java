package com.ecom.domain.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import com.ecom.domain.user.AppUser;

@Entity
@Table(name = "orders")
public class AppOrder {
	@Id
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private AppUser user;

	@Column(nullable = false)
	private String customerName;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false)
	private String city;

	@Column(nullable = false)
	private String country;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus status;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal total;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderItem> items = new ArrayList<>();

	@Column(nullable = false)
	private Instant createdAt;

	protected AppOrder() {
	}

	public AppOrder(
		AppUser user,
		String customerName,
		String email,
		String address,
		String city,
		String country
	) {
		this.id = UUID.randomUUID();
		this.user = user;
		this.customerName = customerName;
		this.email = email;
		this.address = address;
		this.city = city;
		this.country = country;
		this.status = OrderStatus.PAID;
		this.total = BigDecimal.ZERO;
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

	public AppUser getUser() {
		return user;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getEmail() {
		return email;
	}

	public String getAddress() {
		return address;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void addItem(OrderItem item) {
		this.items.add(item);
		item.setOrder(this);
		recalculateTotal();
	}

	public void recalculateTotal() {
		this.total = this.items
			.stream()
			.map(OrderItem::getLineTotal)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
