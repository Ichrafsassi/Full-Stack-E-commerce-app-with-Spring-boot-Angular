package com.ichrafsassi.ecommerce.repository;

import com.ichrafsassi.ecommerce.domain.Order;
import com.ichrafsassi.ecommerce.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);
    List<Order> findAllByOrderByCreatedAtDesc();
}
