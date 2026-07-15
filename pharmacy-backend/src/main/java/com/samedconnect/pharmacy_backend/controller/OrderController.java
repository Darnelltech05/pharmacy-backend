package com.samedconnect.pharmacy_backend.controller;

import com.samedconnect.pharmacy_backend.dto.request.CreateOrderRequest;
import com.samedconnect.pharmacy_backend.dto.request.UpdateOrderStatusRequest;
import com.samedconnect.pharmacy_backend.dto.response.OrderResponse;
import com.samedconnect.pharmacy_backend.entity.User;
import com.samedconnect.pharmacy_backend.service.OrderService;
import com.samedconnect.pharmacy_backend.utils.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateOrderRequest request) {

        OrderResponse response =
                orderService.createOrder(user.getId(), request);

        return ResponseEntity.ok(
                Response.success("Order created successfully", response)
        );
    }

    @GetMapping
    public ResponseEntity<Response<List<OrderResponse>>> getMyOrders(
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(
                Response.success(
                        "Orders retrieved successfully",
                        orderService.getMyOrders(user.getId())
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<OrderResponse>> getOrderById(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {

        return ResponseEntity.ok(
                Response.success(
                        "Order retrieved successfully",
                        orderService.getOrderById(user.getId(), id)
                )
        );
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Response<OrderResponse>> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request) {

        return ResponseEntity.ok(
                Response.success(
                        "Order status updated successfully",
                        orderService.updateOrderStatus(id, request)
                )
        );
    }
}