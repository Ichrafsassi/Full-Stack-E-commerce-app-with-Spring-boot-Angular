package com.ichrafsassi.ecommerce.service;

import com.ichrafsassi.ecommerce.domain.Category;
import com.ichrafsassi.ecommerce.dto.CategoryDto;
import com.ichrafsassi.ecommerce.exception.BadRequestException;
import com.ichrafsassi.ecommerce.exception.ResourceNotFoundException;
import com.ichrafsassi.ecommerce.repository.CategoryRepository;
import com.ichrafsassi.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream().map(CategoryDto::from).toList();
    }

    public CategoryDto findById(Long id) {
        return CategoryDto.from(getEntity(id));
    }

    public CategoryDto create(CategoryDto dto) {
        Category c = Category.builder()
                .name(dto.name())
                .description(dto.description())
                .imageUrl(dto.imageUrl())
                .build();
        return CategoryDto.from(categoryRepository.save(c));
    }

    public CategoryDto update(Long id, CategoryDto dto) {
        Category c = getEntity(id);
        c.setName(dto.name());
        c.setDescription(dto.description());
        c.setImageUrl(dto.imageUrl());
        return CategoryDto.from(categoryRepository.save(c));
    }

    public void delete(Long id) {
        if (productRepository.existsByCategory_Id(id)) {
            throw new BadRequestException("Category still has products. Remove or reassign them first.");
        }
        categoryRepository.delete(getEntity(id));
    }

    public Category getEntity(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }
}
