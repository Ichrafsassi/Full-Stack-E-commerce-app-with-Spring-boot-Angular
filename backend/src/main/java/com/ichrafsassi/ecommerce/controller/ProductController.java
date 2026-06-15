package com.ichrafsassi.ecommerce.controller;

import com.ichrafsassi.ecommerce.dto.ProductDto;
import com.ichrafsassi.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDto> list(@RequestParam(required = false) Long categoryId,
                                 @RequestParam(required = false) String search) {
        return productService.search(categoryId, search);
    }

    @GetMapping("/{id}")
    public ProductDto get(@PathVariable Long id) {
        return productService.findById(id);
    }
}
