package com.samedconnect.pharmacy_backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MedicineResponse {

    private Long id;

    private String name;

    private String description;

    private String category;

    private BigDecimal price;

    private Integer stockQuantity;

    private Boolean requiresPrescription;

    private Boolean available;
}