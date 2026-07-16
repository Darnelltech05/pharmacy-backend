package com.samedconnect.pharmacy_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String orderUuid;
    private Long userId;
    private LocalDateTime orderDate;
    private Double totalAmount;    // Can stay as Double for response
    private String status;
    private String shippingAddress;
    private String clinicPickupLocation;
    private List<OrderItemResponse> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemResponse {
        private Long id;
        private Long medicineId;
        private String medicineName;
        private Integer quantity;
        private Double priceAtTime;    // Can stay as Double for response
        private Double subtotal;        // Can stay as Double for response
    }
}