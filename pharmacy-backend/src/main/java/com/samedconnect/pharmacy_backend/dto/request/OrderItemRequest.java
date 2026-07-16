package com.samedconnect.pharmacy_backend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {

    private Long medicineId;

    private Integer quantity;
}