package com.samedconnect.pharmacy_backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemResponse {

    private Long medicineId;
    private String medicineName;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
}