package com.samedconnect.pharmacy_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
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

    private LocalDateTime orderDate;
}