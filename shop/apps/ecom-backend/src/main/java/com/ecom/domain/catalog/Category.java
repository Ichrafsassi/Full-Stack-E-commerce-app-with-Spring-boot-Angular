package com.ecom.domain.catalog;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories")
public class Category {
	@Id
	private UUID id;

	@Column(nullable = false, unique = true)
	private String name;

	@Column(nullable = false, unique = true)
	private String slug;

	@Column(nullable = false)
	private Instant createdAt;

	protected Category() {
	}

	public Category(String name) {
		this.id = UUID.randomUUID();
		this.name = name;
		this.slug = toSlug(name);
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
		if (this.slug == null || this.slug.isBlank()) {
			this.slug = toSlug(this.name);
		}
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSlug() {
		return slug;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setName(String name) {
		this.name = name;
		this.slug = toSlug(name);
	}

	private static String toSlug(String raw) {
		if (raw == null) return "";
		String slug = raw.trim().toLowerCase();
		slug = slug.replaceAll("[^a-z0-9]+", "-");
		slug = slug.replaceAll("(^-+|-+$)", "");
		return slug;
	}
}
