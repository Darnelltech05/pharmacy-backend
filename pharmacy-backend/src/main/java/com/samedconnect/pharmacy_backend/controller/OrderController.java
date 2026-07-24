package com.samedconnect.pharmacy_backend.controller;

import com.samedconnect.pharmacy_backend.dto.request.CreateOrderRequest;
import com.samedconnect.pharmacy_backend.dto.response.OrderItemResponse;
import com.samedconnect.pharmacy_backend.dto.response.OrderResponse;
import com.samedconnect.pharmacy_backend.entity.Order;
import com.samedconnect.pharmacy_backend.entity.OrderItem;
import com.samedconnect.pharmacy_backend.entity.User;
import com.samedconnect.pharmacy_backend.service.OrderService;
import com.samedconnect.pharmacy_backend.utils.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Response<OrderResponse>> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            @AuthenticationPrincipal User user) {

        OrderResponse order = orderService.createOrder(request, user.getId());
        return ResponseEntity.ok(Response.success("Order placed successfully", order));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST')")
    public ResponseEntity<Response<List<OrderResponse>>> getAllOrders() {
        List<OrderResponse> orderResponses = orderService.getAllOrders();
        return ResponseEntity.ok(Response.success("Orders retrieved successfully", orderResponses));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST', 'CUSTOMER')")
    public ResponseEntity<Response<OrderResponse>> getOrderById(@PathVariable Long id) {
        OrderResponse response = orderService.getOrderById(id);
        return ResponseEntity.ok(Response.success("Order retrieved successfully", response));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST') or (hasRole('CUSTOMER') and principal.id == #userId)")
    public ResponseEntity<Response<List<OrderResponse>>> getOrdersByUser(@PathVariable Long userId) {
        List<OrderResponse> orderResponses = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(Response.success("Orders retrieved successfully", orderResponses));
    }

    @GetMapping("/history")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Response<List<OrderResponse>>> getOrderHistory(
            @AuthenticationPrincipal User user) {
        List<OrderResponse> orderResponses = orderService.getOrdersByUser(user.getId());
        return ResponseEntity.ok(Response.success("Order history retrieved", orderResponses));
    }
}