package com.ichrafsassi.ecommerce.repository;

import com.ichrafsassi.ecommerce.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
