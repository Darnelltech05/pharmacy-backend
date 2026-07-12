package com.samedconnect.pharmacy_backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record MedicineResponse(
        Long id,
        String name,
        String category,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        LocalDate expiryDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
