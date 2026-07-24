package com.samedconnect.pharmacy_backend.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MedicineRequest(
        @NotBlank(message = "Medicine name is required")
        String name,

        @NotBlank(message = "Medicine category is required")
        String category,

        String description,

        @NotNull(message = "Medicine price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Medicine price must be greater than zero")
        BigDecimal price,

        @NotNull(message = "Stock quantity is required")
        @PositiveOrZero(message = "Stock quantity cannot be negative")
        Integer stockQuantity,

        Boolean isArchived,

        Boolean requiresPrescription,

        @NotNull(message = "Expiry date is required")
        @FutureOrPresent(message = "Expiry date cannot be in the past")
        LocalDate expiryDate
) {
}
