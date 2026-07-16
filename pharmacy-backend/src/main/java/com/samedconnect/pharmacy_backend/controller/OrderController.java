package com.samedconnect.pharmacy_backend.controller;

import com.samedconnect.pharmacy_backend.dto.request.CreateOrderRequest;
import com.samedconnect.pharmacy_backend.dto.response.OrderResponse;
import com.samedconnect.pharmacy_backend.entity.Order;
import com.samedconnect.pharmacy_backend.entity.User;
import com.samedconnect.pharmacy_backend.exception.ResourceNotFoundException;
import com.samedconnect.pharmacy_backend.service.OrderService;
import com.samedconnect.pharmacy_backend.utils.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Response<OrderResponse>> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            @AuthenticationPrincipal User user) {

        OrderResponse order = orderService.createOrder(request, user.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Response.success("Order placed successfully", order));
    }

    @GetMapping
    public ResponseEntity<Response<List<Order>>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(Response.success("Orders retrieved successfully", orders));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Order>> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(Response.success("Order retrieved successfully", order));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Response<List<Order>>> getOrdersByUser(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(Response.success("Orders retrieved successfully", orders));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<Response<List<Order>>> getMyOrders(@AuthenticationPrincipal User user) {
        List<Order> orders = orderService.getOrdersByUser(user.getId());
        return ResponseEntity.ok(Response.success("Your orders retrieved successfully", orders));
    }
}