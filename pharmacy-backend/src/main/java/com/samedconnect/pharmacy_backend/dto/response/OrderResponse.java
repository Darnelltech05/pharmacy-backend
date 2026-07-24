package com.samedconnect.pharmacy_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String orderUuid;
    private BigDecimal totalAmount;
    private String status;
    private String shippingAddress;
    private String clinicPickupLocation;
    private String prescriptionUrl;
    private LocalDateTime orderDate;
    private List<OrderItemResponse> items;
}