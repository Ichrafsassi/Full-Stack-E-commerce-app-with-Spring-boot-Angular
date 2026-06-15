package com.ichrafsassi.ecommerce.service;

import com.ichrafsassi.ecommerce.domain.*;
import com.ichrafsassi.ecommerce.dto.CheckoutRequest;
import com.ichrafsassi.ecommerce.dto.OrderCreateRequest;
import com.ichrafsassi.ecommerce.dto.OrderDto;
import com.ichrafsassi.ecommerce.dto.OrderItemRequest;
import com.ichrafsassi.ecommerce.dto.OrderUpdateRequest;
import com.ichrafsassi.ecommerce.exception.BadRequestException;
import com.ichrafsassi.ecommerce.exception.ResourceNotFoundException;
import com.ichrafsassi.ecommerce.repository.CartRepository;
import com.ichrafsassi.ecommerce.repository.OrderRepository;
import com.ichrafsassi.ecommerce.repository.ProductRepository;
import com.ichrafsassi.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final PaymentService paymentService;

    @Transactional(readOnly = true)
    public List<OrderDto> myOrders(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user).stream().map(OrderDto::from).toList();
    }

    @Transactional(readOnly = true)
    public List<OrderDto> allOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc().stream().map(OrderDto::from).toList();
    }

    public OrderDto findById(Long id) {
        return OrderDto.from(orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found")));
    }

    public OrderDto findByIdForUser(Long id, User user) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (!order.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Access denied");
        }
        return OrderDto.from(order);
    }

    @Transactional
    public OrderDto checkout(User user, CheckoutRequest request) {
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new BadRequestException("Cart is empty"));
        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.PENDING)
                .shippingAddress(request.shippingAddress())
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem ci : cart.getItems()) {
            Product p = ci.getProduct();
            if (p.getStock() < ci.getQuantity()) {
                throw new BadRequestException("Insufficient stock for " + p.getName());
            }
            BigDecimal line = p.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity()));
            order.getItems().add(OrderItem.builder()
                    .order(order)
                    .productId(p.getId())
                    .productName(p.getName())
                    .unitPrice(p.getPrice())
                    .quantity(ci.getQuantity())
                    .lineTotal(line)
                    .build());
            p.setStock(p.getStock() - ci.getQuantity());
            productRepository.save(p);
            total = total.add(line);
        }
        order.setTotalAmount(total);

        if (request.useStripe()) {
            String intentId = paymentService.createSimulatedIntent(total);
            order.setPaymentIntentId(intentId);
            order.setStatus(OrderStatus.PAID);
        }

        order = orderRepository.save(order);
        cartService.clear(user);
        return OrderDto.from(order);
    }

    @Transactional
    public OrderDto createOrder(OrderCreateRequest request) {
        User user = userRepository.findByEmail(request.customerEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with email: " + request.customerEmail()));

        Order order = Order.builder()
                .user(user)
                .status(request.status())
                .shippingAddress(request.shippingAddress())
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemRequest reqItem : request.items()) {
            Product p = productRepository.findById(reqItem.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + reqItem.productId()));
            if (p.getStock() < reqItem.quantity()) {
                throw new BadRequestException("Insufficient stock for " + p.getName());
            }
            BigDecimal line = p.getPrice().multiply(BigDecimal.valueOf(reqItem.quantity()));
            order.getItems().add(OrderItem.builder()
                    .order(order)
                    .productId(p.getId())
                    .productName(p.getName())
                    .unitPrice(p.getPrice())
                    .quantity(reqItem.quantity())
                    .lineTotal(line)
                    .build());
            p.setStock(p.getStock() - reqItem.quantity());
            productRepository.save(p);
            total = total.add(line);
        }
        order.setTotalAmount(total);
        order = orderRepository.save(order);
        return OrderDto.from(order);
    }

    @Transactional
    public OrderDto updateOrder(Long id, OrderUpdateRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Restore stock of previous items back to products
        for (OrderItem oldItem : order.getItems()) {
            productRepository.findById(oldItem.getProductId()).ifPresent(p -> {
                p.setStock(p.getStock() + oldItem.getQuantity());
                productRepository.save(p);
            });
        }

        order.setStatus(request.status());
        order.setShippingAddress(request.shippingAddress());
        order.getItems().clear();

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemRequest reqItem : request.items()) {
            Product p = productRepository.findById(reqItem.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + reqItem.productId()));
            if (p.getStock() < reqItem.quantity()) {
                throw new BadRequestException("Insufficient stock for " + p.getName());
            }
            BigDecimal line = p.getPrice().multiply(BigDecimal.valueOf(reqItem.quantity()));
            order.getItems().add(OrderItem.builder()
                    .order(order)
                    .productId(p.getId())
                    .productName(p.getName())
                    .unitPrice(p.getPrice())
                    .quantity(reqItem.quantity())
                    .lineTotal(line)
                    .build());
            p.setStock(p.getStock() - reqItem.quantity());
            productRepository.save(p);
            total = total.add(line);
        }
        order.setTotalAmount(total);
        order = orderRepository.save(order);
        return OrderDto.from(order);
    }

    @Transactional
    public OrderDto updateStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setStatus(status);
        return OrderDto.from(orderRepository.save(order));
    }

    @Transactional
    public void delete(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        
        // Restore stock on order delete
        for (OrderItem oldItem : order.getItems()) {
            productRepository.findById(oldItem.getProductId()).ifPresent(p -> {
                p.setStock(p.getStock() + oldItem.getQuantity());
                productRepository.save(p);
            });
        }

        orderRepository.delete(order);
    }
}
