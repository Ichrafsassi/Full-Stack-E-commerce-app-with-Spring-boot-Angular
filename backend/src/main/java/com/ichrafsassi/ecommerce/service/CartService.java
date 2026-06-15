package com.ichrafsassi.ecommerce.service;

import com.ichrafsassi.ecommerce.domain.Cart;
import com.ichrafsassi.ecommerce.domain.CartItem;
import com.ichrafsassi.ecommerce.domain.User;
import com.ichrafsassi.ecommerce.dto.CartDto;
import com.ichrafsassi.ecommerce.dto.CartItemRequest;
import com.ichrafsassi.ecommerce.exception.BadRequestException;
import com.ichrafsassi.ecommerce.exception.ResourceNotFoundException;
import com.ichrafsassi.ecommerce.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;

    @Transactional(readOnly = true)
    public CartDto getCart(User user) {
        return toDto(getOrCreateCart(user));
    }

    @Transactional
    public CartDto addItem(User user, CartItemRequest request) {
        Cart cart = getOrCreateCart(user);
        var product = productService.getEntity(request.productId());
        if (product.getStock() < request.quantity()) {
            throw new BadRequestException("Insufficient stock");
        }
        var existing = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(product.getId()))
                .findFirst();
        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + request.quantity());
        } else {
            cart.getItems().add(CartItem.builder().cart(cart).product(product).quantity(request.quantity()).build());
        }
        return toDto(cartRepository.save(cart));
    }

    @Transactional
    public CartDto updateQuantity(User user, Long productId, int quantity) {
        Cart cart = getOrCreateCart(user);
        var item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item not in cart"));
        if (quantity <= 0) {
            cart.getItems().remove(item);
        } else {
            if (item.getProduct().getStock() < quantity) {
                throw new BadRequestException("Insufficient stock");
            }
            item.setQuantity(quantity);
        }
        return toDto(cartRepository.save(cart));
    }

    @Transactional
    public CartDto removeItem(User user, Long productId) {
        Cart cart = getOrCreateCart(user);
        cart.getItems().removeIf(i -> i.getProduct().getId().equals(productId));
        return toDto(cartRepository.save(cart));
    }

    @Transactional
    public void clear(User user) {
        Cart cart = getOrCreateCart(user);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user).orElseGet(() ->
                cartRepository.save(Cart.builder().user(user).build()));
    }

    private CartDto toDto(Cart cart) {
        var items = cart.getItems().stream()
                .map(i -> new CartDto.CartItemDto(
                        i.getId(), i.getProduct().getId(), i.getProduct().getName(),
                        i.getProduct().getPrice(), i.getQuantity(),
                        i.getProduct().getPrice().multiply(BigDecimal.valueOf(i.getQuantity())),
                        i.getProduct().getImageUrl()))
                .toList();
        BigDecimal total = items.stream().map(CartDto.CartItemDto::lineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new CartDto(cart.getId(), items, total);
    }
}
