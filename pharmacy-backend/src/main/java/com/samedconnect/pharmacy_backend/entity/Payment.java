package com.samedconnect.pharmacy_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "payments")
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Every payment belongs to one order
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "payment_uuid", nullable = false, unique = true)
    private String paymentUuid;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(name = "transaction_reference")
    private String transactionReference;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @PrePersist
    protected void onCreate() {

        paymentUuid = UUID.randomUUID().toString();

        paymentDate = LocalDateTime.now();

        if (status == null) {
            status = PaymentStatus.COMPLETED;
        }

        if (transactionReference == null) {
            transactionReference = "TXN-" + System.currentTimeMillis();
        }
    }

    public enum PaymentMethod {
        CARD,
        CASH,
        MOBILE
    }

    public enum PaymentStatus {
        PENDING,
        COMPLETED,
        FAILED
    }
}