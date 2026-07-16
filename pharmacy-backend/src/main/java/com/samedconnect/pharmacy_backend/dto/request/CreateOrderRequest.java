package com.samedconnect.pharmacy_backend.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateOrderRequest {

    private Long userId;

    private String shippingAddress;

    private String clinicPickupLocation;

    private List<OrderItemRequest> items;
}