package com.ichrafsassi.ecommerce.repository;

import com.ichrafsassi.ecommerce.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByActiveTrue();

    @Query("SELECT p FROM Product p WHERE p.active = true AND " +
           "(:categoryId IS NULL OR p.category.id = :categoryId)")
    List<Product> searchByCategory(@Param("categoryId") Long categoryId);

    @Query("SELECT p FROM Product p WHERE p.active = true AND " +
           "(:categoryId IS NULL OR p.category.id = :categoryId) AND " +
           "(:search IS NULL OR p.name LIKE CONCAT('%', :search, '%'))")
    List<Product> search(@Param("categoryId") Long categoryId, @Param("search") String search);

    List<Product> findTop6ByActiveTrueOrderByIdDesc();

    List<Product> findByActiveTrueAndDiscountPercentGreaterThanOrderByDiscountPercentDesc(int discountPercent);

    boolean existsByCategory_Id(Long categoryId);
}
