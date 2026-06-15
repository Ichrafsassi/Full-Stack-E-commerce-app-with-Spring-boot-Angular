package com.ichrafsassi.ecommerce.controller.admin;

import com.ichrafsassi.ecommerce.domain.OrderStatus;
import com.ichrafsassi.ecommerce.dto.OrderCreateRequest;
import com.ichrafsassi.ecommerce.dto.OrderDto;
import com.ichrafsassi.ecommerce.dto.OrderUpdateRequest;
import com.ichrafsassi.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderDto> list() {
        return orderService.allOrders();
    }

    @GetMapping("/{id}")
    public OrderDto get(@PathVariable Long id) {
        return orderService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto create(@Valid @RequestBody OrderCreateRequest request) {
        return orderService.createOrder(request);
    }

    @PutMapping("/{id}")
    public OrderDto update(@PathVariable Long id, @Valid @RequestBody OrderUpdateRequest request) {
        return orderService.updateOrder(id, request);
    }

    @PatchMapping("/{id}/status")
    public OrderDto updateStatus(@PathVariable Long id, @RequestBody Map<String, OrderStatus> body) {
        return orderService.updateStatus(id, body.get("status"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        orderService.delete(id);
    }
}
