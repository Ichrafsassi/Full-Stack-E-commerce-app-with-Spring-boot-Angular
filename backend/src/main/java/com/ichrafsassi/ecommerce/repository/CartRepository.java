package com.ichrafsassi.ecommerce.repository;

import com.ichrafsassi.ecommerce.domain.Cart;
import com.ichrafsassi.ecommerce.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
