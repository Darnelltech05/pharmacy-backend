package com.samedconnect.pharmacy_backend.repository;

import com.samedconnect.pharmacy_backend.entity.Order;
import com.samedconnect.pharmacy_backend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Check if an order already has a payment
    Optional<Payment> findByOrder(Order order);

    // Find a payment by its UUID
    Optional<Payment> findByPaymentUuid(String paymentUuid);

    boolean existsByOrder(Order order);
}