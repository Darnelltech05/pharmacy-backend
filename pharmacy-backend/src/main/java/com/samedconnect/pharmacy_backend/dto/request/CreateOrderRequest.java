package com.samedconnect.pharmacy_backend.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {

    @NotBlank(message = "Pickup location is required")
    private String pickupLocation;

    @Valid
    @NotEmpty(message = "Order must contain at least one medicine")
    private List<CreateOrderItemRequest> items;
}