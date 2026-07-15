package com.samedconnect.pharmacy_backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponse {

    private Long paymentId;

    private String paymentUuid;

    private Long orderId;

    private BigDecimal amount;

    private String paymentMethod;

    private String status;

    private String transactionReference;

    private LocalDateTime paymentDate;
}