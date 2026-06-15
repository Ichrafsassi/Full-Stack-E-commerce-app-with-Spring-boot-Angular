package com.ichrafsassi.ecommerce.service;

import com.ichrafsassi.ecommerce.config.TechProductCatalog;
import com.ichrafsassi.ecommerce.domain.Product;
import com.ichrafsassi.ecommerce.dto.ProductDto;
import com.ichrafsassi.ecommerce.dto.ProductRequest;
import com.ichrafsassi.ecommerce.exception.ResourceNotFoundException;
import com.ichrafsassi.ecommerce.repository.ProductRepository;
import com.ichrafsassi.ecommerce.util.PricingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public List<ProductDto> search(Long categoryId, String search) {
        String normalizedSearch = (search != null && !search.isBlank()) ? search : null;
        var products = normalizedSearch == null
                ? productRepository.searchByCategory(categoryId)
                : productRepository.search(categoryId, normalizedSearch);
        return products.stream().map(ProductDto::from).toList();
    }

    public List<ProductDto> findAllForAdmin() {
        return productRepository.findAll().stream().map(ProductDto::from).toList();
    }

    public ProductDto findById(Long id) {
        return ProductDto.from(getEntity(id));
    }

    public ProductDto create(ProductRequest request) {
        Product p = mapRequest(new Product(), request);
        return ProductDto.from(productRepository.save(p));
    }

    public ProductDto update(Long id, ProductRequest request) {
        Product p = mapRequest(getEntity(id), request);
        return ProductDto.from(productRepository.save(p));
    }

    @Transactional
    public void delete(Long id) {
        Product p = getEntity(id);
        p.setActive(false);
        productRepository.save(p);
    }

    public Product getEntity(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    private Product mapRequest(Product p, ProductRequest request) {
        p.setName(request.name());
        p.setDescription(request.description());
        p.setStock(request.stock());
        p.setImageUrl(request.imageUrl());
        p.setActive(request.active());
        if (request.categoryId() != null) {
            p.setCategory(categoryService.getEntity(request.categoryId()));
        }
        if (request.imageUrl() == null || request.imageUrl().isBlank()) {
            p.setImageUrl(TechProductCatalog.imageFor(p.getName(), p.getCategory()));
        }
        BigDecimal original = request.originalPrice() != null ? request.originalPrice() : request.price();
        int discount = request.discountPercent() != null ? request.discountPercent() : 0;
        PricingUtil.applyTo(p, original, discount);
        return p;
    }
}
