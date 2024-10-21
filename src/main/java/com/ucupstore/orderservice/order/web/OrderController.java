package com.ucupstore.orderservice.order.web;

import com.ucupstore.orderservice.order.domain.Order;
import com.ucupstore.orderservice.order.domain.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Flux<Order> getOrder() {
        return orderService.findAll();
    }

    @PostMapping
    public Mono<Order> createOrder(@RequestBody @Valid OrderRequest order) {
        return orderService.submitOrder(order.isbn(), order.quantity());
    }
}
