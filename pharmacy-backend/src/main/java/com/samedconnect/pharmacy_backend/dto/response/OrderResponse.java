package com.samedconnect.pharmacy_backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {

    private Long id;

    private String customerName;

    private String status;

    private BigDecimal totalAmount;

    private String pickupLocation;

    private LocalDateTime orderDate;

    private List<OrderItemResponse> items;
}